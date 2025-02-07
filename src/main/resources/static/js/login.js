const form = document.getElementById('login-form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let loginRequest = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        loginRequest[key] = value;
    }

    fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginRequest)
    }).then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                const {accessToken} = data;
                localStorage.setItem('accessToken', accessToken);
                window.location.href = '/';
            });
        }
        else response.json().then((data) => alert(data.message));
    }).catch((error) => {
        alert(error);
    });

});
