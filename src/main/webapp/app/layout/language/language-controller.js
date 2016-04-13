"use strict";

angular.module('app').controller("LanguagesCtrl",  function LanguagesCtrl($scope, $rootScope, $log, Language){

    $rootScope.lang = {};

    Language.getLanguages(function(data){

        $rootScope.currentLanguage = data[1];

        $rootScope.languages = data;

        Language.getLang(data[1].key,function(data){

            $rootScope.lang = data;
        });

    });

    $scope.selectLanguage = function(language){
        $rootScope.currentLanguage = language;

        Language.getLang(language.key,function(data){

            $rootScope.lang = data;

        });
    }

    $rootScope.getWord = function(key){
        if(angular.isDefined($rootScope.lang[key])){
            return $rootScope.lang[key];
        }
        else {
            return key;
        }
    }

});
