import {Link} from "react-router-dom";
import React from 'react';

function Header(props) {
    return (
        <header>
            <nav>
                <li><Link to='/register'>Registracija</Link></li>
                <li><Link to='/login'>Prijava</Link></li>
                <li><Link to='/'>Zemljevid</Link></li>
                <li><Link to='/'>Grafični prikaz</Link></li>
                <li><Link to='/scrapper'>Stanje na cestah</Link></li>
            </nav>
        </header>
    );
}

export default Header;