appCtrllers.controller('profileCtrl', function ($scope, $window, $state, $cookies, FinApp) {
    $scope.user = $cookies.getObject('userInfo');

    FinApp.currentUser().then(function(res){
        $scope.user=res.data;
    });
    FinApp.getGroups().then(function(res){
        $scope.groups=res.data;
    })

    $scope.createGroup=function(groupName){
        var group={"name":groupName,"label":groupName};
        FinApp.createGroup(group).then(function(res){
            $scope.newGroupForm=false;
        });
    };

    $scope.logout = function () {
        $cookies.remove("userInfo");
        FinApp.logout();
        $state.go('login');
        $window.location.reload();
    };
});
