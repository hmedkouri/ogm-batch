package modules

import com.google.inject.AbstractModule
import com.mycila.guice.ext.closeable.CloseableModule
import com.mycila.guice.ext.jsr250.Jsr250Module

class AnnotationsModule extends AbstractModule {
    AnnotationsModule() {
    }

    protected void configure() {
        this.install(new CloseableModule())
        this.install(new Jsr250Module())
    }
}