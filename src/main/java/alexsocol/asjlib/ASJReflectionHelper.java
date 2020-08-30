package alexsocol.asjlib;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

/**
 * The class prev named CSReflection.
 * <p>
 * This class adds several utils for "hacking" into the JVM, also known as Reflection.
 * 
 * @author Clashsoft; slightly improved by AlexSocol
 */
public class ASJReflectionHelper {
	
	public static final Field modifiersField;
	
	static {
		Field f = null;
		try {
			f = Field.class.getDeclaredField("modifiers");
			f.setAccessible(true);
		} catch (Throwable ignored) { }
		modifiersField = f;
	}
	
	/**
	 * Adds the modifiers {@code mod} to the given {@link Field} {@code field} if
	 * {@code flag} is true, and removed them otherwise.
	 * 
	 * @param field the field
	 * @param mod the modifiers
	 * @param flag add or remove
	 */
	public static void setModifier(Field field, int mod, boolean flag) {
		try {
			field.setAccessible(true);
			int modifiers = modifiersField.getInt(field);
			if (flag) {
				modifiers |= mod;
			} else {
				modifiers &= ~mod;
			}
			modifiersField.setInt(field, modifiers);
		} catch (Throwable ex) {
			CSLog.error(ex);
		}
	}
	
	// Caller-sensitive
	
	/**
	 * Returns the caller {@link Class}.
	 * 
	 * @return the called class.
	 */
	public static Class getCallerClass() {
		try {
			return Class.forName(getCallerClassName());
		} catch (ClassNotFoundException ex) {
			CSLog.error(ex);
			return null;
		}
	}
	
	/**
	 * @return the name of the caller class.
	 */
	public static String getCallerClassName() {
		StackTraceElement ste = getCaller();
		return ste != null ? ste.getClassName() : "java.lang.Object";
	}
	
	/**
	 * @return the caller {@link StackTraceElement}
	 */
	public static StackTraceElement getCaller() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
	
		for (int i = 1; i < stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			String className = ste.getClassName();
			
			if (!ASJReflectionHelper.class.getName().equals(className) && !className.startsWith("java.lang.Thread")) {
				if (callerClassName == null) {
					callerClassName = className;
				} else if (!callerClassName.equals(className)) {
					return ste;
				}
			}
		}
		
		return null;
	}
	
	// Methods
	
	/**
	 * Returns the method of the given {@link Class} {@code class} specified by the
	 * given {@code object}.
	 * <ul>
	 * <li>If {@code object} is a {@link Method} instance, it returns the object.
	 * <li>If {@code object} is an integer, it returns the {@link Method} of the
	 * given {@link Class} {@code class} with the id {@code object}.
	 * <li>If {@code object} is an Object[] of length 2, it
	 * <ul>
	 * <li>Returns the method with the name {@code object[0]} if {@code object[0]}
	 * is a String
	 * <li>Returns the method with the name of any {@code object[0]} if
	 * {@code object[0]} is a String[]
	 * </ul>
	 * </ul>
	 * 
	 * @param clazz the clazz
	 * @param object the object
	 * @return the method
	 */
	public static Method getMethod(Class clazz, Object object) {
		if (object == null) {
			throw new NullPointerException("Cannot get null method!");
		}
		Class c = object.getClass();
		if (c == Method.class) {
			return (Method) object;
		} else if (c == int.class) {
			return getMethod(clazz, ((Integer) object).intValue());
		} else if (c == Object[].class) {
			Object[] aobject = (Object[]) object;
			if (aobject.length == 2) {
				if (aobject[0] instanceof String) {
					return getMethod(clazz, (String) aobject[0], (Class[]) aobject[1]);
				} else if (aobject[0] instanceof String[]) {
					return getMethod(clazz, (String[]) aobject[0], (Class[]) aobject[1]);
				}
			}
		}
		CSLog.error("Unable to get method specified with " + object);
		return null;
	}
	
	/**
	 * Returns the {@link Method} of the given {@link Class} {@code clazz} with the
	 * given name {@code methodName} and the given parameter types
	 * {@code parameterTypes}.
	 * 
	 * @param clazz the clazz
	 * @param methodName the method name
	 * @param parameterTypes the parameter types
	 * @return the method
	 */
	public static Method getMethod(Class clazz, String methodName, Class[] parameterTypes) {
		try {
			Method m = clazz.getDeclaredMethod(methodName, parameterTypes);
			if (m != null) {
				return m;
			}
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the {@link Method} of the given {@link Class} {@code clazz} with a
	 * name contained in {@code methodNames} and the given parameter types
	 * {@code parameterTypes}.
	 * 
	 * @param clazz the clazz
	 * @param methodNames the possible method names
	 * @param parameterTypes the parameter types
	 * @return the method
	 */
	public static Method getMethod(Class clazz, String[] methodNames, Class[] parameterTypes) {
		for (String methodName : methodNames) {
			Method m = getMethod(clazz, methodName, parameterTypes);
			if (m != null) {
				return m;
			}
		}
		CSLog.error(new NoSuchMethodException("Method not found! (Class: " + clazz + "; Expected field names: " + Arrays.toString(methodNames)));
		return null;
	}
	
	/**
	 * Returns the {@link Method} of the given {@link Class} {@code clazz} with the
	 * given method ID {@code methodID}.
	 * 
	 * @param clazz the clazz
	 * @param methodID the method ID
	 * @return the method
	 */
	public static Method getMethod(Class clazz, int methodID) {
		return clazz.getDeclaredMethods()[methodID];
	}
	
	// Method invocation
	
	// Reference
	
	public static <T, R> R invokeStatic(Class<? super T> clazz, Object[] args, Object method) {
		return invoke(clazz, null, args, method);
	}
	
	public static <T, R> R invoke(T instance, Object[] args, Object method) {
		return invoke((Class<T>) instance.getClass(), instance, args, method);
	}
	
	public static <T, R> R invoke(Class<? super T> clazz, T instance, Object[] args, Object method) {
		Method m = getMethod(clazz, method);
		return invoke(m, instance, args);
	}
	
	// Method ID
	
	public static <T, R> R invokeStatic(Class<? super T> clazz, Object[] args, int methodID) {
		return invoke(clazz, null, args, methodID);
	}
	
	public static <T, R> R invoke(T instance, Object[] args, int methodID) {
		return invoke((Class<T>) instance.getClass(), instance, args, methodID);
	}
	
	public static <T, R> R invoke(Class<? super T> clazz, T instance, Object[] args, int methodID) {
		Method m = getMethod(clazz, methodID);
		return invoke(m, instance, args);
	}
	
	/**
	 * Directly invokes the given {@link Method} {@code method} on the given
	 * {@link Object} {@code instance} with the given arguments {@code args} and
	 * returns the result.
	 * 
	 * @param method the method to invoke
	 * @param instance the instance
	 * @param args the arguments
	 * @return the result
	 */
	public static <T, R> R invoke(Method method, Object instance, Object[] args) {
		try {
			method.setAccessible(true);
			return (R) method.invoke(instance, args);
		} catch (Exception ex) {
			CSLog.error(ex);
			return null;
		}
	}
	
	// Fields
	
	public static <T> T[] getStaticObjects(Class clazz, Class<T> fieldType, boolean subtypes) {
		return getObjects(clazz, null, fieldType, subtypes);
	}
	
	public static <T> T[] getObjects(Class clazz, Object instance, Class<T> fieldType, boolean subtypes) {
		List list = new ArrayList();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			try {
				Class c = field.getType();
				Object o = field.get(instance);
				if (c == fieldType || subtypes && fieldType.isAssignableFrom(c)) {
					list.add(o);
				}
			} catch (Throwable ignore) { }
		}
		
		return (T[]) list.toArray();
	}
	
	// Fields
	
	/**
	 * Returns the {@link Field} of the given {@link Class} {@code clazz} with the
	 * name {@code name}.
	 * 
	 * @param clazz the clazz
	 * @param name the field name
	 * @return the field
	 */
	public static Field getField(Class clazz, String name) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (name.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}
	
	/**
	 * Returns the {@link Field} of the given {@link Class} {@code clazz} with a
	 * name contained in {@code fieldNames}.
	 * 
	 * @param clazz the clazz
	 * @param fieldNames the possible field names
	 * @return the field
	 */
	public static Field getField(Class clazz, String... fieldNames) {
		Field[] fields = clazz.getDeclaredFields();
		for (String fieldName : fieldNames) {
			for (Field field : fields) {
				if (fieldName.equals(field.getName())) {
					return field;
				}
			}
		}
		CSLog.error(new NoSuchFieldException("Field not found! (Class: " + clazz + "; Expected field names: " + Arrays.toString(fieldNames)));
		return null;
	}
	
	/**
	 * Returns the {@link Field} of the given {@link Class} {@code clazz} with the
	 * field ID {@code fieldID}
	 * 
	 * @param clazz the clazz
	 * @param fieldID the field ID
	 * @return the field
	 */
	public static Field getField(Class clazz, int fieldID) {
		return clazz.getDeclaredFields()[fieldID];
	}
	
	// Field getters
	
	// Reference
	
	public static <T, R> R getStaticValue(Class<? super T> clazz, String... fieldNames) {
		return getValue(clazz, null, fieldNames);
	}
	
	public static <T, R> R getValue(T instance, String... fieldNames) {
		return getValue((Class<T>) instance.getClass(), instance, fieldNames);
	}
	
	public static <T, R> R getValue(Class<? super T> clazz, T instance, String... fieldNames) {
		Field f = getField(clazz, fieldNames);
		return getValue(f, instance);
	}
	
	// Field ID
	
	public static <T, R> R getStaticValue(Class<? super T> clazz, int fieldID) {
		return getValue(clazz, null, fieldID);
	}
	
	public static <T, R> R getValue(T instance, int fieldID) {
		return getValue((Class<? super T>) instance.getClass(), instance, fieldID);
	}
	
	public static <T, R> R getValue(Class<? super T> clazz, T instance, int fieldID) {
		Field f = getField(clazz, fieldID);
		return getValue(f, instance);
	}
	
	public static <T> T getValue(Field field, Object instance) {
		return getValue(field, instance, true);
	}
	
	/**
	 * Directly gets the value of the given {@link Field} on the given
	 * {@link Object} {@code instance}.
	 * 
	 * @param field the field to get
	 * @param instance the instance
	 * @param checkAccessible true if field is private
	 * @return the value
	 */
	public static <T> T getValue(Field field, Object instance, boolean checkAccessible) {
		try {
			if (checkAccessible) field.setAccessible(true);
			return (T) field.get(instance);
		} catch (Exception ex) {
			CSLog.error(ex);
			return null;
		}
	}
	
	// Field setters
	
	// 
	
	public static <T, V> void setStaticValue(Class<? super T> clazz, V value, String... fieldNames) {
		setValue(clazz, null, value, fieldNames);
	}
	
	public static <T, V> void setValue(T instance, V value, String... fieldNames) {
		setValue((Class<? super T>) instance.getClass(), instance, value, fieldNames);
	}
	
	public static <T, V> void setValue(Class<? super T> clazz, T instance, V value, String... fieldNames) {
		Field f = getField(clazz, fieldNames);
		setValue(f, instance, value);
	}
	
	// Field ID
	
	public static <T, V> void setStaticValue(Class<? super T> clazz, V value, int fieldID) {
		setValue(clazz, null, value, fieldID);
	}
	
	public static <T, V> void setValue(T instance, V value, int fieldID) {
		setValue((Class<? super T>) instance.getClass(), instance, value, fieldID);
	}
	
	public static <T, V> void setValue(Class<? super T> clazz, T instance, V value, int fieldID) {
		Field f = getField(clazz, fieldID);
		setValue(f, instance, value);
	}
	
	public static <T, V> void setValue(Field field, T instance, V value) {
		setValue(field, instance, value, true);
	}
	
	/**
	 * Directly sets the value of the given {@link Field} on the given
	 * {@link Object} {@code instance} to the given {@link Object} {@code value} .
	 * 
	 * @param field the field to set
	 * @param instance the instance
	 * @param value the new value
	 * @param checkAccessible true if field is private
	 */
	public static <T, V> void setValue(Field field, T instance, V value, boolean checkAccessible) {
		try {
			if(checkAccessible) field.setAccessible(true);
			field.set(instance, value);
		} catch (Exception ex) {
			CSLog.error(ex);
		}
	}
	
	// Reference
	
	public static <T, V> void setStaticFinalValue(Class<? super T> clazz, V value, String... fieldNames) {
		setFinalValue(clazz, null, value, fieldNames);
	}
	
	public static <T, V> void setFinalValue(T instance, V value, String... fieldNames) {
		setFinalValue((Class<? super T>) instance.getClass(), instance, value, fieldNames);
	}
	
	public static <T, V> void setFinalValue(Class<? super T> clazz, T instance, V value, String... fieldNames) {
		Field f = getField(clazz, fieldNames);
		setFinalValue(f, instance, value);
	}
	
	// Field ID
	
	public static <T, V> void setStaticFinalValue(Class<? super T> clazz, V value, int fieldID) {
		setFinalValue(clazz, null, value, fieldID);
	}
	
	public static <T, V> void setFinalValue(T instance, V value, int fieldID) {
		setFinalValue((Class<? super T>) instance.getClass(), instance, value, fieldID);
	}
	
	public static <T, V> void setFinalValue(Class<? super T> clazz, T instance, V value, int fieldID) {
		Field f = getField(clazz, fieldID);
		setFinalValue(f, instance, value);
	}
	
	public static <T, V> void setFinalValue(Field field, T instance, V value) {
		setFinalValue(field, instance, value, true);
	}
	
	public static <T, V> void setFinalValue(Field field, T instance, V value, boolean checkAccessible) {
		try {
			if (checkAccessible) field.setAccessible(true);
			int mods = field.getModifiers();
			modifiersField.setInt(field, mods & ~Modifier.FINAL);
			field.set(instance, value);
			modifiersField.setInt(field, mods);
		} catch (Exception ex) {
			CSLog.error(ex);
		}
	}
	
	// Instances
	
	public static <T> T createInstance(String className) {
		try {
			Class c = Class.forName(className);
			return (T) c.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static <T> T createInstance(Class<T> c) {
		try {
			return c.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static <T> T createInstance(Class<T> c, Object... parameters) {
		Class[] parameterTypes = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				parameterTypes[i] = parameters[i].getClass();
			}
		}
	
		return createInstance(c, parameterTypes, parameters);
	}
	
	public static <T> T createInstance(Class<T> c, Class[] parameterTypes, Object... parameters) {
		try {
			Constructor<T> constructor = c.getConstructor(parameterTypes);
			return constructor.newInstance(parameters);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static class CSLog {
		public static final CSLogger logger = new CSLogger();
		
		public static void print(String string) {
			logger.log(Level.INFO, string);
		}
		
		public static void info(String string) {
			logger.log(Level.INFO, string);
		}
		
		public static void warning(String string) {
			logger.log(Level.WARNING, string);
		}
		
		public static void error(String string) {
			logger.log(Level.SEVERE, string);
		}
		
		public static void print(Object object) {
			print(String.valueOf(object));
		}
		
		public static void info(Object object) {
			info(String.valueOf(object));
		}
		
		public static void warning(Object object) {
			warning(String.valueOf(object));
		}
		
		public static void error(Object object) {
			error(String.valueOf(object));
		}
		
		public static void error(Throwable throwable) {
			logger.log(Level.SEVERE, throwable);
		}
		
		public static void print(String format, Object... args) {
			print(String.format(format, args));
		}
		
		public static void info(String format, Object... args) {
			info(String.format(format, args));
		}
		
		public static void warning(String format, Object... args) {
			warning(String.format(format, args));
		}
		
		public static void error(String format, Object... args) {
			error(String.format(format, args));
		}
		
		public static class CSLogger {
			public static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
			public void log(Level level, String msg) {
				log.log(level, msg);
			}
			
			public void log(Level level, Throwable error) {
				log.log(level, error.getMessage(), error);
			}
		}
		
	}
}