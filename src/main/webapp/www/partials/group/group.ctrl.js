(function () {
    angular.module('app.controllers')
        .controller('Group',
            ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'commons', Group]);
    function Group($scope, fin, $stateParams, $rootScope, $ionicPopup, commons){
        var groupId = $stateParams.groupId;
    }
})()
