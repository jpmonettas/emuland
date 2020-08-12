(ns emuland.undoable-map)

(defprotocol IUndoable
  (undo [_]))

(deftype UndoableMap [hist]
  ILookup
  (-lookup [um k]
    (get (first (.-hist um)) k))

  IAssociative
  (-contains-key? [um k]
    (contains? (first (.-hist um)) k))

  (-assoc [um k v]
    (UndoableMap. (conj (.-hist um) (assoc (first (.-hist um)) k v))))

  IMap
  (-dissoc [um k]
    (UndoableMap. (conj (.-hist um) (dissoc (first (.-hist um)) k))))

  IPrintWithWriter
  (-pr-writer [um writer opts]
    (write-all writer (str "#undoable " (first (.-hist um)))))

  ICounted
  (-count [um]
    (count (first (.-hist um))))

  ISeqable
  (-seq [um]
    (seq (first (.-hist um))))

  IUndoable
  (undo [um]
    (if (> (count (.-hist um)) 1)
      (UndoableMap. (rest (.-hist um)))
      um)))

(defn make-undoable-map [init-map]
  (UndoableMap. (list init-map)))
