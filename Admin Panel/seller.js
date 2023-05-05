function getdata() {
    firebase.database().ref('Users').once('value', function (snapshot) {
        snapshot.forEach(function (childSnapshot) {
            //const users = childSnapshot.val()
            if (childSnapshot.val().accountType == 'Seller') {
                var name = childSnapshot.val().name;
                var email = childSnapshot.val().email;
                var phone = childSnapshot.val().phone;
                var uid = childSnapshot.val().uid;
                //id=childSnapshot.val().uid
                createelement(name,email,phone,uid)

                console.log(name,email,phone,uid)

            }
            /*var name = childSnapshot.val().name;
            var email = childSnapshot.val().email;
            var phone = childSnapshot.val().phone;
            var uid = childSnapshot.val().uid;
            //id=childSnapshot.val().uid
            createelement(name, email, phone, uid)*/

        });
    })
}

//window.onload = getdata
function createelement(name, email, phone, uid) {
    const root = document.querySelector('.card')
    const node1 = document.createElement('div')
    node1.className = "img"

    root.appendChild(node1)
    const node2 = document.createElement('div')
    node2.className = 'infos'
    const childnode2 = document.createElement('div')
    childnode2.className = 'name'
    const h = document.createElement('h2')
    h.className = "selname"
    h.textContent = name
    childnode2.appendChild(h)
    node2.appendChild(childnode2)
    const h2 = document.createElement('h4')
    h2.textContent = "Email"
    node2.appendChild(h2)
    const p = document.createElement('p')
    p.className = "Email"
    p.textContent = email
    node2.appendChild(p)
    const h3 = document.createElement('h4')
    h3.textContent = "Phone No."
    node2.appendChild(h3)
    const p2 = document.createElement('p')
    p2.className = "phone"
    p2.textContent = phone
    node2.appendChild(p2)
    const ul = document.createElement('ul')
    ul.className = "id"
    const li = document.createElement('li')
    const h4 = document.createElement('h4')
    h4.textContent = "ID"
    const p3 = document.createElement('p')
    p3.className = "selid"
    p3.textContent = uid
    ul.appendChild(li)
    li.appendChild(h4)
    li.appendChild(p3)
    node2.appendChild(ul)
    const node3 = document.createElement('div')
    node3.className = "profile"
    const btn = document.createElement('button')
    btn.className = "view"
    const a = document.createElement('a')
    const url = `sellerprofile.html?uid=${uid}`
    a.href = url
    a.textContent = "View profile"
    btn.appendChild(a)
    node3.appendChild(btn)
    node2.appendChild(node3)
    root.appendChild(node2)
}
window.onload = getdata