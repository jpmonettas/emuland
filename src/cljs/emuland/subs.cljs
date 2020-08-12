(ns emuland.subs
  (:require
   [re-frame.core :as re-frame]
   [clj-avr.emulator :as emu]
   [clj-avr.disassembler :as da]
   ))

(re-frame/reg-sub
 :emulator/emulator
 (fn [{:keys [emulator-hist]}]
   (first emulator-hist)))

(re-frame/reg-sub
 :emulator/prog-mem
 :<- [:emulator/emulator]
 (fn [emulator]
   (:prog-mem emulator)))

(re-frame/reg-sub
 :emulator/memory-segments
 :<- [:emulator/emulator]
 (fn [emulator]
   (let [seg-size 16
         mem-map (->> (range emu/ram-end)
                      (map #(emu/read-mem-byte emulator %)))
         segments (partition seg-size mem-map)
         addresses (iterate #(+ % seg-size) 0)]
      (map vector addresses segments))))

(re-frame/reg-sub
 :prog/disassemble
 :<- [:emulator/prog-mem]
 (fn [prog-mem]
   (let [data (emu/mem-data prog-mem)
         max-addr-written (emu/mem-used prog-mem)]
     (->> data
          (take (inc max-addr-written))
          da/disassemble-bytes
          (da/add-instr-mem-addresses 0)))))

(re-frame/reg-sub
 :prog/used-registers
 :<- [:prog/disassemble]
 (fn [prog-instructions [_]]
   (let [named-regs-set (into #{:pc} (keys emu/reg->addr))]
    (->> prog-instructions
         (mapcat (fn [{:keys [src-reg dst-reg reg]}]
                   [src-reg dst-reg reg]))
         (remove nil?)
         (into named-regs-set)))))

(re-frame/reg-sub
 :cpu/registers
 :<- [:emulator/emulator]
 (fn [emulator]
   (:regs emulator)))

(re-frame/reg-sub
 :emu/used-registers-name-val
 :<- [:cpu/registers]
 :<- [:prog/used-registers]
 (fn [[registers used-registers] [_]]
   (->> used-registers
        (map (fn [r]
               (let [rn (if (keyword? r)
                          (name r)
                          (str "r" r))
                     rv (get registers r 0)]
                 [rn rv])))
        (sort-by first))))

(re-frame/reg-sub
 :cpu/program-counter
 :<- [:cpu/registers]
 (fn [regs]
   (:pc regs)))
