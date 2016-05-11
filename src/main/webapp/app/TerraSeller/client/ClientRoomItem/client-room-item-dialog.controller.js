(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomItemDialogController', ClientRoomItemDialogController);

    ClientRoomItemDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientRoomItem'];

    function ClientRoomItemDialogController ($scope, $stateParams, $uibModalInstance, entity, ClientRoomItem) {
        var vm = this;
        vm.clientRoomItem = entity;
        vm.load = function(id) {
            ClientRoomItem.get({id : id}, function(result) {
                vm.clientRoomItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('app.terraSeller:clientRoomItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.clientRoomItem.id !== null) {
                ClientRoomItem.update(vm.clientRoomItem, onSaveSuccess, onSaveError);
            } else {
                ClientRoomItem.save(vm.clientRoomItem, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
