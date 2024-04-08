package solution;

// target = method
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

//retention policy = runtime
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


//annotation then:

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Then {
 String value() default "";
}
