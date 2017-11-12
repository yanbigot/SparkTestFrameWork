$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("DataSetStep.feature");
formatter.feature({
  "comments": [
    {
      "line": 1,
      "value": "# new feature"
    },
    {
      "line": 2,
      "value": "# Tags: optional"
    }
  ],
  "line": 4,
  "name": "A description",
  "description": "",
  "id": "a-description",
  "keyword": "Feature"
});
formatter.scenario({
  "line": 6,
  "name": "A scenario",
  "description": "",
  "id": "a-description;a-scenario",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 5,
      "name": "@wip"
    }
  ]
});
formatter.step({
  "line": 7,
  "name": "a list a magnificient children",
  "rows": [
    {
      "cells": [
        "id",
        "name"
      ],
      "line": 8
    },
    {
      "cells": [
        "1",
        "Saskia"
      ],
      "line": 9
    },
    {
      "cells": [
        "2",
        "Mark"
      ],
      "line": 10
    }
  ],
  "keyword": "Given "
});
formatter.step({
  "line": 11,
  "name": "I look for Mark",
  "keyword": "When "
});
formatter.step({
  "line": 12,
  "name": "I find him",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
});