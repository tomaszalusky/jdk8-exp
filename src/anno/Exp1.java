package anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Function;

public class Exp1 {

	@Retention(RetentionPolicy.RUNTIME)
	@interface Hoo {
		String value();
	}
	
	@Hoo("hoo")
	static class Foo {
		private final String value1;
		private final String value2;
		public Foo(String value1, String value2) {
			this.value1 = value1;
			this.value2 = value2;
		}
		public String getValue1() {
			return value1;
		}
		public String getValue2() {
			return value2;
		}
	}
	
	static void print(Foo instance, Function<Foo,String> getter) {
		System.out.println(getter.apply(instance));
	}
	
	static void print(Hoo annotation, Function<Hoo,String> annotationProperty) {
		System.out.println(annotationProperty.apply(annotation));
	}
	
	public static void main(String[] args) {
		System.out.println("x");
		Foo foo = new Foo("foo1","foo2");
		print(foo,Foo::getValue1);
		print(foo,Foo::getValue2);
		Hoo hoo = foo.getClass().getAnnotation(Hoo.class);
		System.out.println(hoo.value());
		print(hoo,Hoo::value);
	}

}
