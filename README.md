# Team 05 Senior Project README
This repository contains Team 05's UNR Senior Project, the project will be updated throughout the semester.
NOTE: This project uses the Intuit QBOConceptsTutorial project as a reference. The project can be found in
the following link https://github.com/IntuitDeveloper/QBOConceptsTutorial-Java/blob/master/src/main/java/com/intuit/developer/tutorials/controller/InventoryController.java

## Prerequisites

* Java 1.8
* A developer.intuit.com account
* An app on the developer.intuit.com and the associated client id and client secret

## Running the Code

   * Run:
  ```
   ./gradlew bootRun (Mac OS) or gradlew.bat bootRun (Windows)
   ```

  * View Page Locally:
  ```
    App runs in http://localhost:8080/
   ```

## Project Notes
The project notes will be updated as changes are made to the project.

**Date:** 01/22/2020
* **Current Functionality:**
    - Utilizes format and buttons given by Quickbooks Tutorials: were all modified to reflect the buttons that will be included in the actual website
    - User is able to connect/login using "Connect to Quickbooks Button"
    - Button clicks return different queries to the Quickbooks API to test being able to retrieve data
    - Data format has been modified for easier processing

* **Future Modifications:**
    - Graph display development
    - editing tools for graph modifications
    - implementation of machine learning algorithms to make predictions
    - UI changes

**Date:** 02/04/2020
* NOTE: Please let us know if the line endings are changed in the gradle file again. It may revert the line endings to Windows again.
    The file permissions may have been reverted as well.
* **Modifications:**
  - Initial changes to UI: Center text and buttons for home page. Place buttons to the side and center returned text in connected page.
  - Testing React components with the created "Like" button, will be deleted later
  - Changes to build, settings file for gradle was deleted.

* **Current Functionality:**
   - User is able to connect/login using "Connect to Quickbooks Button"
   - Button clicks return different queries to the Quickbooks API to test being able to retrieve data
   - Data format has been modified for easier processing

* **Future Modifications:**
   - Graph display development
   - Exporting data to pdf reports
   - Implementation of machine learning algorithms to make predictions, or general predictions
   - Further UI changes

**Date:** 02/24/2020
* **Modifications:**
- Changes to Login Page
- Addition of Side and Top Nav Bars
- Format user data for User button
- Addition of controllers to format data
- Include WEKA (for Machine Learning) library in build

* **Future Modifications:**
  - Graph display development
  - Exporting data to pdf reports
  - Implementation of machine learning algorithms to make predictions, or general predictions
  - Further UI changes, connect other buttons and change layout of pages

**Date:** 03/09/2020
* **Note:**
- This folder shows the most current version of the project before creating the new React app
- The backend includes information for the Account data tab and more parsing for the machine learning model
- The separate new React app is located in a different branch --LM

**Date:** 03/21/2020
* **Note:**
- Addition of Jackson for marshalling of JSON objects
- Changes to User controller to test with the React app -LM

**Date:** 04/03/2020
* **Note:**
- Refactor to connect to the React App
- Addition of the oauthController, logoutController
- Work in Progress: Refactor the backend controllers to return and accept what is needed for the React app
- The project includes files that are no longer needed but kept for reference

**Date:** 04/09/2020
* **Note:**
- Refactor to connect to the React App
- Addition of the machine learning portions
- Addition of the pdf generation
- Work in Progress: Display graphs to the UI, connect the PDF generation

**Date:** 04/12/2020
* **Note:**
- Refactor logout controller
- Work in Progress: Display graphs to tghe UI, generate graphs, connect the PDF generation

