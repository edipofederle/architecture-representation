package mestrado.arquitetura.parser.method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
 
public class Types {
 
	public interface Type {
		String getName();
	}
 
	/*
	 * Primitive Representations
	 */
	public static final Type BOOLEAN = new Type() {
		public String getName() {
			return "boolean";
		}
	};
	public static final Type BYTE = new Type() {
		public String getName() {
			return "byte";
		}
	};
	public static final Type CHAR = new Type() {
		public String getName() {
			return "char";
		}
	};
	public static final Type SHORT = new Type() {
		public String getName() {
			return "short";
		}
	};
	public static final Type INTEGER = new Type() {
		public String getName() {
			return "int";
		}
	};
	public static final Type DOUBLE = new Type() {
		public String getName() {
			return "double";
		}
	};
	public static final Type FLOAT = new Type() {
		public String getName() {
			return "float";
		}
	};
	public static final Type LONG = new Type() {
		public String getName() {
			return "long";
		}
	};
 
	/*
	 * Wrapper Representations
	 */
	public static final Type BOOLEAN_WRAPPER = new Type() {
		public String getName() {
			return "Boolean";
		}
	};
	public static final Type BYTE_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Byte";
		}
	};
	public static final Type CHAR_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Character";
		}
	};
	public static final Type SHORT_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Short";
		}
	};
	public static final Type INTEGER_WRAPPER = new Type() {
		public String getName() {
			return "Integer";
		}
	};
	public static final Type DOUBLE_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Double";
		}
	};
	public static final Type FLOAT_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Float";
		}
	};
	public static final Type LONG_WRAPPER = new Type() {
		public String getName() {
			return "java.lang.Long";
		}
	};
 
	/*
	 * String
	 */
	public static final Type STRING = new Type() {
		public String getName() {
			return "String";
		}
	};
 
	/*
	 * Custom Type
	 */
	public static Type custom(final String customType) {
		return new Type() {
			public String getName() {
				return customType;
			}
		};
	}
 
	public static boolean isCustomType(String userType) {
		boolean custom = true;
		for (Field type : getNativeTypes()) {
			try {
				if (type.getName().equalsIgnoreCase(userType)) {
					custom = false;
					break;
				}
			} catch (Exception e) { /* who cares?? */ } 
		}
		return custom;
	}
 
	private static List<Field> getNativeTypes() {
		List<Field> staticFields = new ArrayList<Field>();
		for (Field field : Types.class.getDeclaredFields())
			if (Modifier.isStatic(field.getModifiers()))
				staticFields.add(field);
		return staticFields;
	}
}