<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:semantic="http://www.omg.org/spec/BPMN/20100524/MODEL"
	xmlns:Namespace="http://www.omg.org/spec/BPMN/20100524/MODEL"
	xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI">
   
	<xsl:output omit-xml-declaration="no" indent="yes"/>
	
    <xsl:strip-space elements="*"/>

 <xsl:template match="node()|@*">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>

<xsl:template match="//semantic:laneSet/semantic:lane|semantic:childLaneSet">
    <xsl:apply-templates/>
</xsl:template>
	
<xsl:template match="bpmndi:BPMNShape[@bpmnElement = //semantic:laneSet/semantic:lane/@id]"/>

 <xsl:template match="//semantic:eventDefinitionRef">
	 <xsl:copy-of select="//semantic:timerEventDefinition"/>
 </xsl:template>

 <xsl:template match="semantic:definitions/semantic:timerEventDefinition"/>
	
</xsl:stylesheet>
