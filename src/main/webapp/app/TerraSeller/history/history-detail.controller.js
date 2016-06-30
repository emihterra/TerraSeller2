(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('HistoryDetailController', HistoryDetailController);

    HistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'History'];

    function HistoryDetailController($scope, $rootScope, $stateParams, entity, History) {
        var vm = this;
        vm.history = entity;
        vm.load = function (id) {
            History.get({id: id}, function(result) {
                vm.history = result;
            });
        };
        var unsubscribe = $rootScope.$on('schoolApp:historyUpdate', function(event, result) {
            vm.history = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
