import { useEffect, useContext } from 'react';
import { UserContext } from '../userContext';
import { Navigate } from 'react-router-dom';
import React, { Component }  from 'react';
function Logout(){
    const userContext = useContext(UserContext); 
    useEffect(function(){
        const logout = async function(){
            userContext.setUserContext(null);
            const res = await fetch("http://146.212.216.121:8080/users/logout");
        }
        logout();
    }, []);

    return (
        <Navigate replace to="/map" />
    );
}

export default Logout;