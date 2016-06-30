(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('HistoryController', HistoryController);

    HistoryController.$inject = ['$scope', '$state', 'History'];

    function HistoryController ($scope, $state, History) {
        var vm = this;
        vm.histories = [];
        vm.loadAll = function() {
            History.query(function(result) {
                vm.histories = result;
            });
        };

        vm.loadAll();
        
    }
})();
