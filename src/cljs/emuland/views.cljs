(ns emuland.views
  (:require
   [re-frame.core :as re-frame]
   [emuland.subs :as subs]
   [clj-avr.disassembler :as da]
   [clj-avr.emulator :as emu]
   [clj-avr.utils :as avr-utils]
   ))

(defn control-panel []
  [:div.control-panel
   [:button {:on-click #(re-frame/dispatch [:emu/step-prev])} "Prev"]
   [:button {:on-click #(re-frame/dispatch [:emu/step-next])} "Next"]
   ])

(defn disassembler-panel []
  (let [insts @(re-frame/subscribe [:prog/disassemble])
        pc @(re-frame/subscribe [:cpu/program-counter])]
    [:div.disassembler-panel
     (for [i insts]
       ^{:key (str (:memory/address i))}
       [:div.instruction (when (= pc (:memory/address i)) {:class "active"})
        (da/disassemble-inst-str i)])]))

(defn memory-panel []
  (let [mem-segs @(re-frame/subscribe [:emulator/memory-segments])]
    [:div.memory-panel
     (for [[addr seg] mem-segs]
       ^{:key (str addr)}
       [:div.addr-line
        [:div.address (str "0x" (avr-utils/padded-hex addr 4))]
        (for [[i b] (map-indexed vector seg)]
          ^{:key (str addr i)}
          [:div.byte (avr-utils/padded-hex b 2)])])]))

(defn regs-panel []
  (let [used-regs @(re-frame/subscribe [:emu/used-registers-name-val])]
    [:div.regs-panel
     (for [[rn rv] used-regs]
       ^{:key rn}
       [:div.reg
        [:div.reg-name rn ]
        [:div.reg-val (avr-utils/padded-hex rv  (if (= rn "pc") 4 2))]])]))

(defn main-panel []
  [:div.main
   [control-panel]
   [regs-panel]
   [memory-panel]
   [disassembler-panel]])
