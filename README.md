This repository have been archived and exists for historical purposes. 
No updates or futher development will go into this repository. The content can be used as is but no support will be given. 

---

newspaper-manualQA-flagger
==========================

This component runs various tests on a batch to check for issues which require assessment via manual QA. The application
outputs a flagging report which lists issues to be checked together with links to the .jp2 files to be checked.

## Configuration

In addition to the standard configuration parameters for autonomous components, this component requires the following
properties (values given below are dummy-examples):

    #Manufacturer(s) of known and tested scanners (comma-separated list)
    flag.mix.scannermanufacturers=ScannerCo A/S

    #Name(s) of known and tested scanner models (comma-separated list)
    flag.mix.scannermodels=scanpro, scanstationplus

    #Model number(s) for known and tested scanners
    flag.mix.modelnumbers=x325, x326

    #Serial number(s) of known and tested scanners
    flag.mix.scannerserialnos=SN#543, SN#610

    #Software names and version numbers of known and tested scanning software, in the form
    #<software name>;<version no>, <software name>;<version no> etc.
    flag.mix.scannersoftwares=scansoft;0.1alpha,scansoft;1.0

    #Name(s) of known image producers (comma separated)
    flag.mix.imageproducers=Statsbiblioteket,Bob,Alice

    #Minimum acceptable OCR accuracy (averaged over edition or film)
    flag.alto.accuracy=60.0

    #Minimum acceptable OCR accuracy per file
    flag.alto.accuracy.perfile=20.0

    #If true, ignore accuracy of exactly zero as this means there is probably no text on
    #the page.
    flag.alto.zeroaccuracy.ignore=true

    #The maximum number of flags that can be created. If more issues are detected then these are reported as a
    #simple count as part of the message of the last flag reported. This is necessary because
    #i) There are no resources to manually check an unlimited number of issues
    #ii) If there are very many issues then they are probably systematic and there is no need to review them all
    #individually, and
    #iii) The backend systems become unstable if one writes overly-large xml documents back to DOMS.
    #
    #An initial value of 100 for this parameter is probably reasonable.
    #
    flag.maxtotalflags=

