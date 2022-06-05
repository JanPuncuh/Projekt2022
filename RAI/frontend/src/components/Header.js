import {Link} from "react-router-dom";
import React from 'react';
import { UserContext } from "../userContext";
//<li><Link to='/'>Grafični prikaz</Link></li>
function Header(props) {
    return (
        <header>
        <nav>
            <ul>
                <UserContext.Consumer>
                    {context => (
                        context.user ?
                            <>
                                <li><Link to='/'>Domov</Link></li>
                                <li><Link to='/scrapper'>Stanje na cestah</Link></li>
                                <li><Link to='/sessions'>Moje Vožnje</Link></li>
                                <li><Link to='/logout'>Odjava</Link></li>
                            </>
                        :
                            <>
                                <li><Link to='/'>Domov</Link></li>
                                <li><Link to='/scrapper'>Stanje na cestah</Link></li>
                                <li><Link to='/login'>Prijava</Link></li>
                                <li><Link to='/register'>Registracija</Link></li>
                            </>

                    )}
                </UserContext.Consumer>
            </ul>
        </nav>
    </header >
    );
}

export default Header;