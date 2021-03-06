package anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This example shows the placement of type annotation on array types
 * so as to have meaning equivalent to collections types.
 * While annotations on collection types are intuitively read from left to right,
 * annotations on arrays have different order due to syntax of array.
 */

public class TypeAnnotations {

	// Nullable* is just for demonstration purpose, no null check is point of this example.
	// Different Nullable* classes help to distinguish particular level of type nesting.
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD,ElementType.TYPE_USE,ElementType.PARAMETER})
	public static @interface NullableC {} // nullable container (array or collection)
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD,ElementType.TYPE_USE,ElementType.PARAMETER})
	public static @interface Nullable1 {} // nullable thing in container
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD,ElementType.TYPE_USE,ElementType.PARAMETER})
	public static @interface Nullable2 {} // nullable thing in thing in container
	
	// Name convention: l = List, a = array, n = Nullable, s = String
	
	// Every two fields in following pairs are equivalent according to type annotations
	// (e.g. the only difference is array vs. collection).
	
	List<String> ls;
	String[] as;
	
	List<@Nullable1 String> lns;
	@Nullable1 String[] ans;

	@NullableC List<String> nls;
	String @NullableC [] nas;

	@NullableC List<@Nullable1 String> nlns;
	@Nullable1 String @NullableC [] nans; // note: annotation for deepest level is at the beginning

	@NullableC List<@Nullable1 List<@Nullable2 String>> nlnlns;
	@Nullable2 String @NullableC [] @Nullable1 [] nanans; // note: annotations for other levels 
	                                                      // are in front of [] and go from left to right
	void m(
			List<@Nullable1 String> lns,
			@Nullable1 String[] ans,
			@NullableC List<String> nls,
			String @NullableC [] nas,
			@NullableC List<@Nullable1 String> nlns,
			@Nullable1 String @NullableC [] nans,
			@NullableC List<@Nullable1 List<@Nullable2 String>> nlnlns,
			@Nullable2 String @NullableC [] @Nullable1 [] nanans
	) {}

	static final Method m = Stream.of(TypeAnnotations.class.getDeclaredMethods()).filter(mm -> "m".equals(mm.getName())).findFirst().get();
	// TODO void anno.TypeAnnotations.m(java.util.List,java.lang.String[],java.util.List,java.lang.String[],java.util.List,java.lang.String[],java.util.List,java.lang.String[][])
	
	// proof of equivalence
	
	public static void main(String[] args) throws Exception {
		testFields();
		testParameters();
	}

	private static void testFields() throws Exception {
		Field ls = field("ls");
		AnnotatedParameterizedType ls_t = (AnnotatedParameterizedType)ls.getAnnotatedType();
		System.out.println(anno(ls_t) + " List of " + anno(ls_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Field lns = field("lns");
		AnnotatedParameterizedType lns_t = (AnnotatedParameterizedType)lns.getAnnotatedType();
		System.out.println(anno(lns_t) + " List of " + anno(lns_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Field ans = field("ans");
		AnnotatedArrayType ans_t = (AnnotatedArrayType)ans.getAnnotatedType();
		System.out.println(anno(ans_t) + " array of " + anno(ans_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Field nls = field("nls");
		AnnotatedParameterizedType nls_t = (AnnotatedParameterizedType)nls.getAnnotatedType();
		System.out.println(anno(nls_t) + " List of " + anno(nls_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Field nas = field("nas");
		AnnotatedArrayType nas_t = (AnnotatedArrayType)nas.getAnnotatedType();
		System.out.println(anno(nas_t) + " array of " + anno(nas_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Field nlns = field("nlns");
		AnnotatedParameterizedType nlns_t = (AnnotatedParameterizedType)nlns.getAnnotatedType();
		System.out.println(anno(nlns_t) + " List of " + anno(nlns_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Field nans = field("nans");
		AnnotatedArrayType nans_t = (AnnotatedArrayType)nans.getAnnotatedType();
		System.out.println(anno(nans_t) + " array of " + anno(nans_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Field nlnlns = field("nlnlns");
		AnnotatedParameterizedType nlnlns_t = (AnnotatedParameterizedType)nlnlns.getAnnotatedType();
		AnnotatedParameterizedType nlnlns_tt = (AnnotatedParameterizedType)nlnlns_t.getAnnotatedActualTypeArguments()[0];
		System.out.println(anno(nlnlns_t) + " List of " + anno(nlnlns_tt) + " List of " + anno(nlnlns_tt.getAnnotatedActualTypeArguments()[0]) + " Strings");

		Field nanans = field("nanans");
		AnnotatedArrayType nanans_t = (AnnotatedArrayType)nanans.getAnnotatedType();
		AnnotatedArrayType nanans_tt = (AnnotatedArrayType)nanans_t.getAnnotatedGenericComponentType();
		System.out.println(anno(nanans_t) + " array of " + anno(nanans_tt) + " array of " + anno(nanans_tt.getAnnotatedGenericComponentType()) + " Strings");
	}

	static void testParameters() throws Exception {
		AnnotatedType lnsType = (AnnotatedType)parameterType("lns");
		System.out.println(anno(lnsType) + " List of " + /*anno(lnsType.getAnnotatedActualTypeArguments()[0]) + */" Strings");
		
		Parameter lns = parameter("lns");
		AnnotatedParameterizedType lns_t = (AnnotatedParameterizedType)lns.getAnnotatedType();
		System.out.println(anno(lns_t) + " List of " + anno(lns_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Parameter ans = parameter("ans");
		AnnotatedArrayType ans_t = (AnnotatedArrayType)ans.getAnnotatedType();
		System.out.println(anno(ans_t) + " array of " + anno(ans_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Parameter nls = parameter("nls");
		AnnotatedParameterizedType nls_t = (AnnotatedParameterizedType)nls.getAnnotatedType();
		System.out.println(anno(nls_t) + " List of " + anno(nls_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Parameter nas = parameter("nas");
		AnnotatedArrayType nas_t = (AnnotatedArrayType)nas.getAnnotatedType();
		System.out.println(anno(nas_t) + " array of " + anno(nas_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Parameter nlns = parameter("nlns");
		AnnotatedParameterizedType nlns_t = (AnnotatedParameterizedType)nlns.getAnnotatedType();
		System.out.println(anno(nlns_t) + " List of " + anno(nlns_t.getAnnotatedActualTypeArguments()[0]) + " Strings");
		
		Parameter nans = parameter("nans");
		AnnotatedArrayType nans_t = (AnnotatedArrayType)nans.getAnnotatedType();
		System.out.println(anno(nans_t) + " array of " + anno(nans_t.getAnnotatedGenericComponentType()) + " Strings");
		
		Parameter nlnlns = parameter("nlnlns");
		AnnotatedParameterizedType nlnlns_t = (AnnotatedParameterizedType)nlnlns.getAnnotatedType();
		AnnotatedParameterizedType nlnlns_tt = (AnnotatedParameterizedType)nlnlns_t.getAnnotatedActualTypeArguments()[0];
		System.out.println(anno(nlnlns_t) + " List of " + anno(nlnlns_tt) + " List of " + anno(nlnlns_tt.getAnnotatedActualTypeArguments()[0]) + " Strings");

		Parameter nanans = parameter("nanans");
		AnnotatedArrayType nanans_t = (AnnotatedArrayType)nanans.getAnnotatedType();
		AnnotatedArrayType nanans_tt = (AnnotatedArrayType)nanans_t.getAnnotatedGenericComponentType();
		System.out.println(anno(nanans_t) + " array of " + anno(nanans_tt) + " array of " + anno(nanans_tt.getAnnotatedGenericComponentType()) + " Strings");
	}
	
	static Field field(String name) throws Exception {
		System.out.println("----------------------------------------------------\n" + name);
		Field result = TypeAnnotations.class.getDeclaredField(name);
		System.out.println("field annotations = " + Arrays.asList(result.getAnnotations()));
		return result;
	}

	static Parameter parameter(String name) throws Exception {
		System.out.println("----------------------------------------------------\n" + name);
		Parameter result = Stream.of(m.getParameters()).filter(p -> p.getName().equals(name)).findFirst().get();
		System.out.println("parameter annotations = " + Arrays.asList(result.getAnnotations()));
		return result;
	}
	
	static AnnotatedType parameterType(String name) throws Exception {
		Parameter[] parameters = m.getParameters();
		Parameter parameter = Stream.of(parameters).filter(p -> p.getName().equals(name)).findFirst().get();
		int index = Arrays.asList(parameters).indexOf(parameter);
		AnnotatedType result = m.getAnnotatedParameterTypes()[index];
		return result;
	}
	
	static String anno(AnnotatedType type) {
		return type.getAnnotations().length == 0 ? "annotationless" : type.getAnnotations()[0].annotationType().getSimpleName();
	}

}

/* OUTPUT:

----------------------------------------------------
lns
annotationless List of Nullable1 Strings
----------------------------------------------------
ans
annotationless array of Nullable1 Strings
----------------------------------------------------
nls
NullableC List of annotationless Strings
----------------------------------------------------
nas
NullableC array of annotationless Strings
----------------------------------------------------
nlns
NullableC List of Nullable1 Strings
----------------------------------------------------
nans
NullableC array of Nullable1 Strings
----------------------------------------------------
nlnlns
NullableC List of Nullable1 List of Nullable2 Strings
----------------------------------------------------
nanans
NullableC array of Nullable1 array of Nullable2 Strings


*/
