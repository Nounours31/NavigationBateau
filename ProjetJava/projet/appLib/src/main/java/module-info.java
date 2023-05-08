module JavaNav.appLib.main {
    requires org.slf4j;

    opens sfa.nav.model ;

    exports sfa.nav.model;
    exports sfa.nav.model.tools;
    exports sfa.nav.nav.calculs;
    exports sfa.nav.astro.calculs;
}