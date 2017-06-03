package com.jlb.tools.metamodel.criterion;

/**
 * Enum : E_OPERATOR : Opérateur de requète.
 * 
 * @author JLuc
 *
 */
public enum Operator {

    /**
     * Opérateur d'égalité.
     */
    EQUALS {
        @Override
        public String toString() {
            return "=";
        }
    },
    /**
     * Opérateur Plus grand que.
     */
    GREATER {
        @Override
        public String toString() {
            return ">";
        }
    },
    /**
     * Opérateur Plus petit que.
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
