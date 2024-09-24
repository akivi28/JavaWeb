function Spa() {
    const [login, setLogin] = React.useState("");
    const [password, setPassword] = React.useState("");


    const loginChange = React.useCallback((e)=> setLogin(e.target.value))
    const passwordChange = React.useCallback((e)=> setPassword(e.target.value))
    const authClick = React.useCallback(()=> {
        const credentials = btoa(login+":"+password);
        console.log(credentials);
        fetch("auth",{
            method: "GET",
            headers:{
                "Authorization": "Basic " + credentials
            }
        }).then(r => r.json().then(console.log))
    })

    return <div>
        <h1>Spa</h1>
        <div>
            <b>Login</b><input onChange={loginChange}/>
            <b>Password</b><input type="password" onChange={passwordChange}/>
            <button onClick={authClick}>Get token</button>
        </div>
    </div>;
}

ReactDOM
    .createRoot(document.getElementById("spa-container"))
    .render(<Spa/>)