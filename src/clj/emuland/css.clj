(ns emuland.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:html {:height "100%"}
   [:body
    {:font-size "10px"
     :font-family "Lucida Console, Monaco, monospace"
     :height "100%"}
    [:div#app {:height "100%"}
     [:.main {:height "98%"
              :display :grid
              :grid-column 5
              :grid-row 6
              :grid-gap "4px"}

      [:.control-panel
       {:grid-row "1 / 2"
        :grid-column "1 / 2"}]
      [:.regs-panel
       {:grid-row "1 / 2"
        :grid-column "2 / 5"
        :border "1px solid #333"}

       [:.reg {:display :inline-block
               :padding "5px"}]]

      [:.memory-panel
       {:grid-row "2 / 7"
        :grid-column "1 / 4"
        :border "1px solid #333"
        :overflow-y :scroll}
       [:.addr-line
        [:.address {:display :inline-block
                    :margin-right "5px"
                    :margin-left "5px"
                    :color "#999"}]
        [:.byte {:display :inline-block
                 :margin-right "5px"
                 :margin-left "5px"}]]]

      [:.disassembler-panel
       {:grid-row "2 / 7"
        :grid-column "4 / 5"
        :border "1px solid #333"
        :padding "5px"
        :overflow-y :scroll}

       [:.instruction
        [:&.active {:background-color :pink}]]]

      ]]]]
)
