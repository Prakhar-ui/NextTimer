import ReactDOM from "react-dom";
import { HashRouter  } from "react-router-dom";
import App from "./App";

const app = document.getElementById("app");


ReactDOM.render(
  <HashRouter >
    <App />
  </HashRouter>,
  app
);
