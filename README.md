# nutch-anth
Anthelion is a Nutch plugin for focused crawling of semantic data.
The project is an open-source project released under Apache License 2.0.

Note: This project contains the complete Nutch 1.6 distribution. The plugin itself can be found in /src/plugin/parse-anth

Table of Contents
-----------------
* [Nutch-Anthelion Plugin](#nutch-anthelion plugin)
  * [Plugin Overview] (#plugin-overview)
  * [Usage and Development] (#usage-and-development)
  * [Some Results] (#some-results)
  * [3rd Party Libraries] (#3rd-party-libraries)
* [Anthelion](#anthelion) 
* [References](#references)

Nutch-Anthelion Plugin
---------
The plugin uses online learning approach to predict data-rich web pages based on the context of the page as well as using feedback from the extraction of metadata from previously seen pages [1].

### Plugin Overview

To perform the focused crawiling the plugin implements three extensions:

1. **AnthelionScoringFilter** (implements the ScoringFilter interface): wraps arround the Anthelion online classifier to classify newly discovered outlinks, as relevant or not. This extension gives score to each outlink, which is then used in the Generate stage, i.e., the URLs for the next fetch cycle are selected based on the score. This extension also pushes feedback to the classifier for the already parsed web pages. The online classifier can be configured and tuned (see [Usage and Development] (#usage and development)).

2. **WdcParser** (implements the Parser interface): This extension parses the web page content and tries to extract semantic data. The parser is adaptation of an already existing nutch parser plugin implemented in [2]. The parser is based on the [any23 library](https://any23.apache.org/) and is able to extract Microdata, Microformats and RDFa annotation from HTML. The extracted triples are stored in the *Content* field.

3. **TripleExtractor** (implements the IndexingFilter interface): This extension stores new fields to the index that can be later used for querying.

An overview of the complete crawling process using the Anthelion plugin is given in the following figure.

<p align="center">
  <img src="https://git.corp.yahoo.com/petar/nutch-anth/blob/master/documentation/architecture.png?raw=true" alt="Architecture"/>
</p>


### Usage and Development

As mentioned in the beginning of the document this project contains the complete Nutch 1.6 code, including the plugin. If you download the complete project, there is no need for any changes and settings. If you want to download only the plugin, please download only the nutch-anth.zip from the root of the folder and go to step 2 of the configuration. If you want to contribute to the plugin and/or want to use the sources with another version of Nutch, please follow the following instructions:

1. Download and copy the /src/plugin/parse-anth folder in your nutch's plugins directory.

2. Enable the plugin in conf/nutch-site.xml by adding *parse-anth* in the *plugin.includes* property.

3. Copy the properties from nutch-anth.xml to conf/nutch-site.xml.

	3.1. Download the baseline.properties file and set the property *anth.scoring.classifier.PropsFilePath* conf/nutch-site.xml to point to the file. This file contains all configurations for the online classifier.

4. In order for ant to compile and deploy the plugin you need to edit the src/plugin/build.xml, by adding the following line in the *deploy* targrt:
	```xml
	<ant dir="parse-anth" target="deploy"/>
	```
5. Add the following lines in conf/parse-plugins.xml:
	```xml
	<mimeType name="text/html">
			<plugin id="parse-anth" />
		</mimeType>
	
	        <mimeType name="application/xhtml+xml">
			<plugin id="parse-anth" />
		</mimeType>
	```
6. Add the following line in the *alias* property in conf/parse-plugins.xml:
	
	```xml
	<alias name="parse-anth" extension-id="com.yahoo.research.parsing.WdcParser" />
	```
7. Copy the *lib* folder to the root of the Nutch distribution.

8. Run `mvn package` inside the *anthelion* folder. This will create the jar "Anthelion-1.0.0-jar-with-dependencies.jar". Copy the jar to src/plugin/parse-anth/lib.

9. Add the following field in conf/schema.xml (also add it to the Solr schema.xml, if you are using Solr):
	```xml
	<field name="containsSem" type="text_general" stored="true" indexed="true"/>
	```
10. Run ant in the root of your folder.

### Some Results

In order to evaluate the focused crawler we measure the precision of the crawled pages, i.e., the ratio of the number of crawled web pages that contain semantic data and the total number of crawled web pages.
So far, we have evaluated using three different seeds sample, and several different configurations. An overview is given in the following table.

<table border=0 cellpadding=0 cellspacing=0 width=532 style='border-collapse:
 collapse;table-layout:fixed;width:532pt'>
 <col width=65 style='width:65pt'>
 <col width=77 style='mso-width-source:userset;mso-width-alt:3285;width:77pt'>
 <col width=65 span=2 style='mso-width-source:userset;mso-width-alt:2773;
 width:65pt'>
 <col class=xl65535 width=65 style='mso-width-source:userset;mso-width-alt:
 2773;width:65pt'>
 <col width=65 span=2 style='mso-width-source:userset;mso-width-alt:2773;
 width:65pt'>
 <col class=xl65535 width=65 style='mso-width-source:userset;mso-width-alt:
 2773;width:65pt'>
 <tr height=15 style='height:15.0pt'>
  <td rowspan=2 height=30 class=xl65 width=65 style='height:30.0pt;width:65pt'>#seeds</td>
  <td rowspan=2 class=xl68 width=77 style='width:77pt'>nutch options</td>
  <td colspan=3 class=xl65 width=195 style='border-left:none;width:195pt'>standard
  scoring</td>
  <td colspan=3 class=xl65 width=195 style='border-left:none;width:195pt'>anthelion
  scoring</td>
 </tr>
 <tr height=15 style='height:15.0pt'>
  <td height=15 class=xl66 style='height:15.0pt;border-top:none;border-left:
  none'>#total pages</td>
  <td class=xl66 style='border-top:none;border-left:none'>#sem pages</td>
  <td class=xl67 style='border-top:none;border-left:none'>precission</td>
  <td class=xl66 style='border-top:none;border-left:none'>#total pages</td>
  <td class=xl66 style='border-top:none;border-left:none'>#sem pages</td>
  <td class=xl67 style='border-top:none;border-left:none'>precission</td>
 </tr>
 <tr height=15 style='height:15.0pt'>
  <td height=15 class=xl66 align=right style='height:15.0pt;border-top:none'>2</td>
  <td class=xl69 style='border-top:none;border-left:none'>-depth 3 -topN 1<span
  style='display:none'>5</span></td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>17</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>2</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.12</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>22</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>7</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.32</td>
 </tr>
 <tr height=15 style='height:15.0pt'>
  <td height=15 class=xl66 align=right style='height:15.0pt;border-top:none'>10</td>
  <td class=xl69 style='border-top:none;border-left:none'>-depth 8 -topN 1<span
  style='display:none'>5</span></td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>99</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>2</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.02</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>49</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>11</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.22</td>
 </tr>
 <tr height=15 style='height:15.0pt'>
  <td height=15 class=xl66 align=right style='height:15.0pt;border-top:none'>1000</td>
  <td class=xl69 style='border-top:none;border-left:none'>-depth 4 -topN 1<span
  style='display:none'>000</span></td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>3200</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>212</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.07</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>2910</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>1469</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.50</td>
 </tr>
 <tr height=15 style='height:15.0pt'>
  <td height=15 class=xl66 align=right style='height:15.0pt;border-top:none'>1000</td>
  <td class=xl70 style='border-top:none;border-left:none'>
  <meta charset=utf-8>
  <span>-depth 5 -topN 2<span style='display:none'>000</span></span></td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>8240</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>511</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.06</td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>
  <meta charset=utf-8>
  <span>9781</span></td>
  <td class=xl66 align=right style='border-top:none;border-left:none'>7587</td>
  <td class=xl67 align=right style='border-top:none;border-left:none'>0.78</td>
 </tr>
</table>

The pairwise comparison is given in the following chart:
<p align="center">
  <img src="https://git.corp.yahoo.com/petar/nutch-anth/blob/master/documentation/results_chart.png?raw=true" alt="Architecture"/>
</p>

### 3rd Party Libraries
The Anthelion plugin uses several 3rd party open source libraries and tools.
Here we summarize the tools used, their purpose, and the licenses under which they're released.

1. This project includes the sources of Apache Nutch 1.6 (Apache License 2.0 - http://www.apache.org/licenses/LICENSE-2.0)
	* http://nutch.apache.org/

2. Apache Any23 1.2 (Apache License 2.0 - http://www.apache.org/licenses/LICENSE-2.0)
	* Used for extraction of semantic anntoation from HTML.
	* https://any23.apache.org/
	* More information about the 3rd party dependencies used in the any23 library can be found [here](https://any23.apache.org/)  

3. The classes com.yahoo.research.parsing.WdcParser and com.yahoo.research.parsing.FilterableTripleHandler are modified version of exiting Nutch plugin (Apache License 2.0 - http://www.apache.org/licenses/LICENSE-2.0)
	* Used for parssing the crawled web pages
	* Hellman et al. [2]; https://www.assembla.com/spaces/commondata/subversion/source/HEAD/extractorNutch

4. For the libraries and tools used in Anthelion, please check the Anthelion [README file] (https://git.corp.yahoo.com/semsearch/nutch-anth/blob/master/anthelion/README.md). 

Anthelion
---------
For more details about the Anthelion project please check the Anthelion [README file] (https://git.corp.yahoo.com/semsearch/nutch-anth/blob/master/anthelion/README.md).

References
----------
[1]. Meusel, Robert, Peter Mika, and Roi Blanco. "Focused Crawling for Structured Data." Proceedings of the 23rd ACM International Conference on Conference on Information and Knowledge Management. ACM, 2014.

[2]. Hellmann, Sebastian, et al. "Knowledge Base Creation, Enrichment and Repair." Linked Open Data--Creating Knowledge Out of Interlinked Data. Springer International Publishing, 2014. 45-69.
	


###Troubleshooting
(TODO)
