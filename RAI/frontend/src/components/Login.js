import { useContext, useState } from 'react';
import { UserContext } from '../userContext';
import { Navigate } from 'react-router-dom';
import React, { Component }  from 'react';
function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const userContext = useContext(UserContext); 

    async function Login(e){
        e.preventDefault();
        const res = await fetch("http://146.212.216.121:8080/users/login", {
            method: "POST",
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
        const data = await res.json();
        if(data._id !== undefined){
            userContext.setUserContext(data);
            window.location.href="/sessions";
        } else {
            setUsername("");
            setPassword("");
            setError("Invalid username or password");
        }
    }

    return (
        <form onSubmit={Login}>
            {userContext.user ? <Navigate replace to="/" /> : ""}
            <input type="text" name="username" placeholder="Username"
             value={username} onChange={(e)=>(setUsername(e.target.value))}/>
             <input type="password" name="password" placeholder="Password"
             value={password} onChange={(e)=>(setPassword(e.target.value))}/>
             <input type="submit" name="submit" value="Log in"/>
             <label>{error}</label>
        </form>
    );
}

export default Login;