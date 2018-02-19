package ansible


args.findAll {
    it.matches(/--(?:list|host\s[\w.]+)/)
}

String arg = null

if (args.any { it == '--list'} ) {


} else if ( (arg = args.find { it =~ /--host [\w.]+/ } )  ) {

    String hostname  = arg.split(/ /)[1]
}

