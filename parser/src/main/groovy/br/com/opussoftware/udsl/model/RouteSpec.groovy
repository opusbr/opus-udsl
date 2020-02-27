package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class RouteSpec extends AbstractSpec {	
	String path;
	String deployment;
	String contract
}

