(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomItemDetailController', ClientRoomItemDetailController);

    ClientRoomItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ClientRoomItem'];

    function ClientRoomItemDetailController($scope, $rootScope, $stateParams, entity, ClientRoomItem) {
        var vm = this;
        vm.clientRoomItem = entity;
        vm.load = function (id) {
            ClientRoomItem.get({id: id}, function(result) {
                vm.clientRoomItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('app.terraSeller:clientRoomItemUpdate', function(event, result) {
            vm.clientRoomItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
