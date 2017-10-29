# guardian-demo

## Note: 

Before building the project, please create a file named ***guardian.properties*** in the root folder. The content is as below:

```
api_key=Your_Guardian_API_KEY_here
```
## Versions
### version 1.1.0
* Implemented with Kotlin

### version 1.0.0
* Basic functions implemented with Java

## Introduction
* A Service named MainService provides methods to get Sections and Contents from Guardian search API.
* A database restores the data from Guardian search API.
* A list shows the web title, section name and publication date.
* A dialog shows all the section to filter the list content. Users could clear the filter to make all the content show.
* A dialog could change the page size.
* A detail view shows the html page from the web url of the content.
* Both views on a single screen is supported.

## Short Statement
The architecture of this application is based on MVVM. 

1. Model
	* Network module based on Retrofit2 is used by main service to communicate with Guardian search API.
	* Database module based on DBFlow restores the data from netwrok and provides it to Views.
2. ViewModel
	* ListViewModel is in charge of display transaction of List View. List View observes the data provided by ListViewModel and modifies its appearance in time. List View also could communicate with ListViewModel by Command.
	* ListViewModel communicates with main service by AIDL as the service works in a different process.
3. View
	* Views communicate with each other by local broadcast.
