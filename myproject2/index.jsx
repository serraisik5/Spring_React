import  ReactDOM  from "react-dom";
import { StrictMode } from "react";
import App from "./App";
//import BasicRouting from ""
import AxiosConfigurer from "./config/AxiosConfigurer";


AxiosConfigurer.configure();

export default function(root){
    root.render(<StrictMode><App /></StrictMode>);
}
