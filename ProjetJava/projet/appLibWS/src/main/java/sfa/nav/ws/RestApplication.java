package sfa.nav.ws;

//--------------------------------------------------------
// https://www.logicbig.com/tutorials/java-ee-tutorial/jax-rs/creating-jax-rs-resources-topic-contents.html
//--------------------------------------------------------

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet(Arrays.asList(CalculAnguilaireRestService.class));
    }
}