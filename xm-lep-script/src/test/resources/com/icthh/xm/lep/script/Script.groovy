package com.icthh.xm.lep.script

def description = "'Logic Extension Point 2017'"
println description

List pointsList = new ArrayList<String>()
pointsList.add("annotation")
pointsList.add("configuration")
pointsList.add("direct")

println "LEP types: "
for (item in pointsList) {
    println " - " + item
}
