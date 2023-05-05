function getdata() {
    firebase.database().ref('Users').once('value', function (snapshot) {
      snapshot.forEach(function (childSnapshot) {
        if (childSnapshot.val().accountType == 'Seller') {
          const id = childSnapshot.val().uid;
  
          firebase.database().ref(`Users/${id}/Orders`).once('value', function (prosnapshot) {
            prosnapshot.forEach(function (prochildsnapshot) {
              // Clone the product template and fill in the data
              const template = document.getElementById('product-template');
              const product = template.cloneNode(true);
              product.id = ''; // Remove the ID attribute from the cloned template
              product.style.display = 'block'; // Make the cloned template visible
              document.getElementById('products').appendChild(product);
  
              // Fill in the data for the cloned product template
             
              const productCategory = product.querySelector('#product-category');
              const productDescription = product.querySelector('#product-description');

              const productTitle = product.querySelector('#product-title');
              const productPrice = product.querySelector('#product-price');
              const productDiscount = product.querySelector('#product-discount');
              const productQuantity = product.querySelector('#product-quantity');
              const producTime = product.querySelector('#product-time');

            
              productCategory.textContent = "Status : "+prochildsnapshot.val().orderStatus;
              productDescription.textContent = "Delivery Fee : "+prochildsnapshot.val().deliveryFee;
              productTitle.textContent = "Price ₹"+prochildsnapshot.val().orderCost;
              productPrice.textContent = "Order To : "+prochildsnapshot.val().orderTo;
              productDiscount.textContent = "Discount ₹"+prochildsnapshot.val().discount;
              productQuantity.textContent = "Order Id : "+prochildsnapshot.val().orderId;

            })
          })
        }
      });
    })
  }
  


window.onload=getdata