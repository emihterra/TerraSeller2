(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDialogController', ClientRoomDialogController);

    ClientRoomDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientRoom'];

    function ClientRoomDialogController ($scope, $stateParams, $uibModalInstance, entity, ClientRoom) {
        var vm = this;
        vm.clientRoom = entity;
        vm.load = function(id) {
            ClientRoom.get({id : id}, function(result) {
                vm.clientRoom = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('app.terraSeller:clientRoomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.clientRoom.id !== null) {
                ClientRoom.update(vm.clientRoom, onSaveSuccess, onSaveError);
            } else {
                ClientRoom.save(vm.clientRoom, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
