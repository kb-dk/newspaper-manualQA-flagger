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
