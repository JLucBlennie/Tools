package com.jlb.tools.metamodel.criterion;

public enum E_OPERATOR {

	EQUALS {
		@Override
		public String toString() {
			return "=";
		}
	},
	GREATER {
		@Override
		public String toString() {
			return ">";
		}
	},
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
