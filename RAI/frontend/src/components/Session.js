
import { Link } from "react-router-dom";
import React, { Component }  from 'react';
function Session(props){
console.log(props);
   // var date = moment(props.photo.date);
    //var dateComponent = date.utc().format('HH:mm:ss - DD-MM-YYYY');

  //  <Link to={{pathname: `/post?id=${props.photo._id}`}}>View Post</Link>
    

    return (
        <div className="card text-dark mb-2">
            <div >     
                <h5 className="card-title">Vožnja: {props.session}</h5>
                <Link to={{pathname: `/map?id=${props.session}`}}>Poglej vožnjo na mapi</Link>
                </div>

    
</div>
    );
}

export default Session;