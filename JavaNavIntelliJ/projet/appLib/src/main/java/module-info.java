module JavaNav.appLib.main {
    requires org.slf4j;

    opens sfa.nav.lib.model ;

    exports sfa.nav.lib.model;
    exports sfa.nav.lib.tools;
    exports sfa.nav.lib.mylittlemath;
}