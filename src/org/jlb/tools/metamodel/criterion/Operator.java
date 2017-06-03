package com.jlb.tools.metamodel.criterion;

/**
 * Enum : E_OPERATOR : Op�rateur de requ�te.
 * 
 * @author JLuc
 *
 */
public enum Operator {

    /**
     * Op�rateur d'�galit�.
     */
    EQUALS {
        @Override
        public String toString() {
            return "=";
        }
    },
    /**
     * Op�rateur Plus grand que.
     */
    GREATER {
        @Override
        public String toString() {
            return ">";
        }
    },
    /**
     * Op�rateur Plus petit que.
     */
    LOWER {
        @Override
        public String toString() {
            return "<";
        }
    }
    // },
    // NOT_EQUALS {
    // @Override
    // public String toString() {
    // return "";
    // }
    // }
}
