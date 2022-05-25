import { useState, useEffect } from 'react';
import Session from './Session';
import { UserContext } from '../userContext';
import React, { Component }  from 'react';
import { useContext } from 'react'
function Sessions(props){
    const [sessions, setSessions] = useState([]);
    const userContext = useContext(UserContext); 
    useEffect(function(){
        const getSessions = async function(){
            const res = await fetch("http://146.212.216.121:8080/cesta/sessions/"+userContext.user.username);
            const data = await res.json();
            setSessions(data);
        }
        getSessions();
    }, []);

    console.log(userContext.user.username);

    return(
        <div>
            <h3>Moje vožnje:</h3>
            <ul>
                {sessions.map(session=>(<Session session={session} key={session.uniqueID}></Session>))}
                
            </ul>
        </div>
    );
}

export default Sessions;