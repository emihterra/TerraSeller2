/**
 * Created by emih on 15.04.2016.
 */

(function(angular) {
    'use strict';

    function ifLoading($http) {
        return {
            restrict: 'A',
            link: function(scope, elem) {
                scope.isLoading = isLoading;

                scope.$watch(scope.isLoading, toggleElement);

                ////////

                function toggleElement(loading) {
                    if (loading) {
                        elem.show();
                    } else {
                        elem.hide();
                    }
                }

                function isLoading() {
                    return $http.pendingRequests.length > 0;
                }
            }
        };
    }

    ifLoading.$inject = ['$http'];

    angular
        .module('app.terraSeller')
        .directive('ifLoading', ifLoading);
}(angular));
