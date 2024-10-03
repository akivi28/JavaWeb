const initialState = {
    auth:{
      token:null
    },
    user: {
      id:null,
      avatar:null,
      name:null,
      role:null
    },
    page: "home",
    shop: {
        categories:[]
    }
};
function reducer(state,action) {
    switch (action.type){
        case 'auth':
            return{
                ...state,
                auth: {
                    ...state.auth,
                    token: action.payload
                }
            };
        case 'navigate':
            return{
                ...state,
                page: action.payload
            };
        case 'setCategory':
            return {
                ...state,
                shop: {
                    ...state.shop,
                    categories: action.payload
                }
            }
        case 'setUser':
            return {
                ...state,
                user: action.payload
            }
    }
    throw Error("Unknown action")
}

const StateContext = React.createContext(null)

function Spa() {
    const [state, dispatch] = React.useReducer(reducer, initialState);

    const [login, setLogin] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [error, setError] = React.useState(false);
    const [isAuth, setAuth] = React.useState(false);
    const [resource, setResource] = React.useState("");

    const loginChange = React.useCallback((e)=> setLogin(e.target.value))
    const passwordChange = React.useCallback((e)=> setPassword(e.target.value))
    const authClick = React.useCallback(() => {
        const credentials = btoa(login + ":" + password);
        console.log(credentials);
        fetch("auth", {
            method: "GET",
            headers: {
                "Authorization": "Basic " + credentials
            }
        }).then(r => {
            if (r.ok) {

                const userId = r.headers.get('X-Claim-Sid');
                const userName = r.headers.get('X-Claim-Name');
                const userAvatar = r.headers.get('X-Claim-Avatar');
                const userRole = r.headers.get('X-Claim-Role');

                const user = { id: userId, name: userName, avatar: userAvatar, role: userRole };
                console.log(user);
                window.sessionStorage.setItem("user", JSON.stringify(user));

                dispatch({
                    type: 'setUser',
                    payload: user
                });

                r.json().then(j => {
                    window.sessionStorage.setItem("token", JSON.stringify(j.data));
                    setAuth(true);
                });
            } else {
                r.json().then(j => {
                    setError(j.data);
                    console.log(j.data);
                });
            }
        });
    });



    const resourceClick = React.useCallback(()=> {
        const token = window.sessionStorage.getItem("token");
        if(!token){
            alert("Requesting a resource in unauthorized mode");
            return;
        }
        fetch("spa",{
            method: "POST",
            headers: {
                'Authorization':'Bearer '+ JSON.parse(token).tokenId,
            }
        }).then(r => r.json()).then(j =>{
            setResource(JSON.stringify(j));
        })
    })

    const exitClick = React.useCallback(()=> {
        window.sessionStorage.removeItem("token");
        setAuth(false);
    })

    const checkToken = React.useCallback(()=>{
        let token = window.sessionStorage.getItem("token");
        if(token){
            token = JSON.parse(token);
            if(new Date(token.exp) < new Date()){
                exitClick();
            }
            else {
                if(!isAuth) {
                    setAuth(true);
                    dispatch({type: 'auth', payload: token})
                };
            }
        }
        else {
            setAuth(false);
        }
    })

    React.useEffect(()=>{
        checkToken();
        const storedUser = window.sessionStorage.getItem("user");
        if (storedUser) {
            dispatch({ type: 'setUser', payload: JSON.parse(storedUser) });
        }
        const interval = setInterval(checkToken, 1000);
        return()=>clearInterval(interval)
    },[])
    
    const navigate = React.useCallback((route)=>{
        dispatch({type: 'navigate', payload: route})
    })

    return <StateContext.Provider value={{state,dispatch}}>
        <h1>Spa</h1>
        {!isAuth &&
            <div>
                <b>Login</b><input onChange={loginChange}/>
                <b>Password</b><input type="password" onChange={passwordChange}/>
                <button onClick={authClick}>Get token</button>
                <br/>
                {error && <b class="error">{error}</b>}
            </div>
        }{ isAuth &&
        <div>
            <button onClick={resourceClick} className="btn deep-purple">Resource</button>
            <button onClick={exitClick} className="btn deep-purple lighten-4">Exit</button>
            <nav className="navbar">
                <div className="nav-wrapper">

                    <ul id="nav-mobile" className="left hide-on-med-and-down">
                        <li><a>{state.user.name}</a></li>
                        <li><a onClick={() => navigate('home')}>Home</a></li>
                        <li><a onClick={() => navigate('shop')}>Shop</a></li>
                    </ul>
                    <img className="nav-avatar left" src={"file/" + state.user.avatar} alt=""/>
                </div>
            </nav>
            <p>{resource}</p>
            {state.page === 'home' && <Home/>}
            {state.page === 'shop' && <Shop/>}
            {state.page.startsWith('category/') && <Category id={state.page.substring(9)}/>}
        </div>
    }
    </StateContext.Provider>;
}

function Category({id}) {
    const {state, dispatch} = React.useContext(StateContext);
    const addProduct = React.useCallback((e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        fetch("shop/product", {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + state.auth.token.tokenId
            },
            body: formData
        }).then(r => r.json()).then(console.log);
    });
    return <React.Fragment>
        <h1>Category: {id}</h1>
        <b onClick={() => dispatch({type: 'navigate', payload: 'home'})}>Home</b>
        {state.auth.token && state.user && state.user.role === 'admin' &&
            <form onSubmit={addProduct} encType="multipart/form-data" className="product-form form-syle">
                <hr/>

                <div className="input-field">
                    <input id="product-name" name="product-name" type="text" className="validate" required/>
                    <label htmlFor="product-name">Назва продукту</label>
                </div>

                <div className="input-field">
                    <input id="product-price" name="product-price" type="number" step="0.01" className="validate"
                           required/>
                    <label htmlFor="product-price">Ціна</label>
                </div>

                <div className="file-field input-field">
                    <div className="btn">
                        <span>Картинка</span>
                        <input type="file" name="product-img" required/>
                    </div>
                    <div className="file-path-wrapper">
                        <input className="file-path validate" type="text" placeholder="Завантажте зображення"/>
                    </div>
                </div>

                <div className="input-field">
                    <textarea id="product-description" name="product-description" className="materialize-textarea"
                              required></textarea>
                    <label htmlFor="product-description">Опис продукту</label>
                </div>

                <input type="hidden" name="product-category-id" value={id}/>

                <button type="submit" className="btn waves-effect waves-light">Додати</button>
            </form>
        }

    </React.Fragment>
}

function Home() {
    const {state, dispatch} = React.useContext(StateContext);
    React.useEffect(() => {
        if (state.shop.categories.length === 0) {
            fetch("shop/category")
                .then(r => r.json())
                .then(j => dispatch({type: 'setCategory', payload: j.data}));
        }
    }, [])
    return <React.Fragment>
        <h2>Home</h2>
        <b onClick={() => dispatch({type: 'navigate', payload: 'shop'})}>Admin</b>
        <div className="shop-caregory-container">
            {state.shop.categories.map(c => <div key={c.id} className="shop-caregory"
                                                 onClick={() => dispatch({
                                                     type: 'navigate',
                                                     payload: 'category/' + c.id
                                                 })}
            >
                <b>{c.name}</b>
                <img src={"file/" + c.imageUrl} alt=""/>
                <p>{c.description}</p>
            </div>)}
        </div>
    </React.Fragment>
}

function Shop() {
    const {state, dispatch} = React.useContext(StateContext);
    const addCategory = React.useCallback((e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        fetch("shop/category", {
            method: 'POST',
            body: formData
        }).then(r => r.json()).then(console.log);
        //console.log(e);
    });
    return <React.Fragment>
        <h2>Shop</h2>

        {state.auth.token && state.user && state.user.role === 'admin' &&
            <form onSubmit={addCategory} encType="multipart/form-data" className="category-form form-syle">
                <hr/>
                <div className="input-field">
                    <input id="category-name" name="category-name" type="text" className="validate" required/>
                    <label htmlFor="category-name">Назва категорії</label>
                </div>

                <div className="file-field input-field">
                    <div className="btn">
                        <span>Картинка</span>
                        <input type="file" name="category-img" required/>
                    </div>
                    <div className="file-path-wrapper">
                        <input className="file-path validate" type="text" placeholder="Завантажте зображення"/>
                    </div>
                </div>

                <div className="input-field">
                    <textarea id="category-description" name="category-description" className="materialize-textarea"
                              required></textarea>
                    <label htmlFor="category-description">Опис</label>
                </div>

                <button type="submit" className="btn waves-effect waves-light">Додати</button>
            </form>

        }
    </React.Fragment>
}

ReactDOM
    .createRoot(document.getElementById("spa-container"))
    .render(<Spa/>)