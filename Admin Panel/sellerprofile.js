function getdata() {
    const urlParams = new URLSearchParams(window.location.search);
    const uid = urlParams.get('uid');
    firebase.database().ref(`Users/${uid}`).once('value', function(snapshot) {
        const sellerData = snapshot.val();
        if(sellerData && sellerData.accountType === 'Seller') {
            document.getElementById('selname').innerHTML = sellerData.name;
            document.getElementById('selemail').innerHTML = sellerData.email;
            document.getElementById('name').value = sellerData.name;
            document.getElementById('phone').value = sellerData.phone;
            document.getElementById('type').value = sellerData.accountType;
            document.getElementById('address').value = sellerData.address;
            document.getElementById('uid').value = sellerData.uid;
            document.getElementById('city').value = sellerData.city;
            document.getElementById('email').value = sellerData.email;
            document.getElementById('shopname').value = sellerData.shopName;
            document.getElementById('fee').value = sellerData.deliveryFee;
            document.getElementById('country').value = sellerData.country;
            document.getElementById('state').value = sellerData.state;
            firebase.database().ref(`Users/${uid}/Ratings`).once('value',function(prosnapshot){
                prosnapshot.forEach(function(prochildsnapshot){
                    document.getElementById('rating').value=prochildsnapshot.val().ratings;
                    document.getElementById('review').value=prochildsnapshot.val().review;
                });
            });
        } else {
            console.log('Seller not found');
        }
    });
}

window.onload=getdata
