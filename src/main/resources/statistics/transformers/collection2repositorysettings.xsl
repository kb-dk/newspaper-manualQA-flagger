<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:old="http://bitrepository.org/settings/CollectionSettings.xsd"
                xmlns="http://bitrepository.org/settings/RepositorySettings.xsd"
                exclude-result-prefixes="old xalan"
                xmlns:redirect="http://xml.apache.org/xalan/redirect"
                extension-element-prefixes="redirect"
                xmlns:xalan="http://xml.apache.org/xslt">
  <xsl:strip-space elements="*"/>
  <xsl:output omit-xml-declaration="yes" indent="yes"/>
  <xsl:template match="/old:CollectionSettings">
    <RepositorySettings xmlns="http://bitrepository.org/settings/RepositorySettings.xsd">
      <xsl:call-template name="getCollections">
      </xsl:call-template>
      <xsl:apply-templates select="*|comment()"/>

    </RepositorySettings>
  </xsl:template>

  <xsl:template match="*">
    <xsl:element name="{local-name(.)}">
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="comment()">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template match="old:CollectionID"></xsl:template>

  <xsl:template match="old:PillarIDs"></xsl:template>

  <xsl:template match="old:ContributorIDs">

    <xsl:variable name="pillarID" select="text()"/>
    <xsl:if test="not(/old:CollectionSettings/old:ClientSettings[old:PillarIDs = $pillarID])">
      <NonPillarContributorIDs>
        <xsl:value-of select="$pillarID"/>
      </NonPillarContributorIDs>
    </xsl:if>
  </xsl:template>


  <xsl:template name="getCollections">
    <Name>
      <xsl:value-of select="old:CollectionID"/>
    </Name>
    <Collections>
      <Collection>
        <ID>
          <xsl:value-of select="old:CollectionID"/>
        </ID>
        <PillarIDs>
          <xsl:for-each select="old:ClientSettings/old:PillarIDs">
            <PillarID>
              <xsl:value-of select="node()">
              </xsl:value-of>
            </PillarID>
          </xsl:for-each>
        </PillarIDs>
      </Collection>
    </Collections>
  </xsl:template>
</xsl:stylesheet>