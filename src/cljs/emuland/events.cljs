(ns emuland.events
  (:require
   [re-frame.core :as re-frame]
   [emuland.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]

   [clj-avr.hex-loader :as hex-loader]
   [clj-avr.emulator :as emu]
   ))

(def hex-str ":100000000C9434000C943E000C943E000C943E0082\r\n:100010000C943E000C943E000C943E000C943E0068\r\n:100020000C943E000C943E000C943E000C943E0058\r\n:100030000C943E000C943E000C943E000C943E0048\r\n:100040000C943E000C943E000C943E000C943E0038\r\n:100050000C943E000C943E000C943E000C943E0028\r\n:100060000C943E000C943E0011241FBECFEFD8E04C\r\n:10007000DEBFCDBF0E9462000C946D000C940000A6\r\n:10008000CF93DF9300D0CDB7DEB79A838983898180\r\n:100090009A81892B19F481E090E00FC089819A81BF\r\n:1000A00001970E944000AC0129813A81429FC00122\r\n:1000B000439F900D529F900D11240F900F90DF9150\r\n:1000C000CF910895CF93DF93CDB7DEB785E090E071\r\n:0E00D0000E944000DF91CF910895F894FFCF79\r\n:00000001FF\r\n")


(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  (-> db/default-db
      (assoc :emulator-hist (list (emu/load-prog
                                   (emu/empty-emu)
                                   (hex-loader/parse-hex hex-str)))))))

(re-frame/reg-event-db
 :emu/step-next
 (fn-traced
  [db _]
  (-> db
      (update :emulator-hist (fn [[e & _ :as eh]] (conj eh (emu/step e)))))))

(re-frame/reg-event-db
 :emu/step-prev
 (fn-traced
  [db _]
  (if (> (-> db :emulator-hist count) 1)
    (-> db
        (update :emulator-hist rest))
    db)))


(comment
  (def hex-str ":100000000C9434000C943E000C943E000C943E0082\r\n:100010000C943E000C943E000C943E000C943E0068\r\n:100020000C943E000C943E000C943E000C943E0058\r\n:100030000C943E000C943E000C943E000C943E0048\r\n:100040000C943E000C943E000C943E000C943E0038\r\n:100050000C943E000C943E000C943E000C943E0028\r\n:100060000C943E000C943E0011241FBECFEFD8E04C\r\n:10007000DEBFCDBF0E9462000C946D000C940000A6\r\n:10008000CF93DF9300D0CDB7DEB79A838983898180\r\n:100090009A81892B19F481E090E00FC089819A81BF\r\n:1000A00001970E944000AC0129813A81429FC00122\r\n:1000B000439F900D529F900D11240F900F90DF9150\r\n:1000C000CF910895CF93DF93CDB7DEB785E090E071\r\n:0E00D0000E944000DF91CF910895F894FFCF79\r\n:00000001FF\r\n")
  (-> (hex-loader/parse-hex hex-str)
      da/disassemble-hex
      da/print-disassemble)

  (let [prog-mem (:prog-mem emu-after-load)
        data (mem-data prog-mem)
        max-addr-written (mem-used mem)]
    (->> data
         (take (inc max-addr-written))
         da/disassemble-bytes
         da/print-disassemble))
  )
