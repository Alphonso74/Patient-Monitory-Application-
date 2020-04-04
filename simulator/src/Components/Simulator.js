import React, { Component } from 'react';
import { connect } from "react-redux";
import {UpdatePatient} from "../Store/Actions/PatientActions";
import firebase from 'firebase';
import 'firebase/firestore';

class Simulator extends Component{

    constructor(props) {
        super(props);
        this.state = {
            text: "Start Simulator",
            running: false
        }
    };

    simButton = () => {
        this.setState((prevState) => ({
            running: !prevState.running
        }));

        this.startSim();
        if(!this.state.running){ this.setState({text: "Start Simulator"}); }
        else{ this.setState({text: "Stop Simulator"}); }
    };

    startSim = () => {
        if(this.state.running){
            let patients = [];
            firebase.firestore().collection('patients').get().then(snapshot => {
                snapshot.forEach(patient => {
                    patients.push(patient.data())
                })}
            ).catch(error => {console.error(error)});

            for (let patient in patients){
                let max;
                switch(patient.tt){
                    case "Blue":
                        max = 100000;
                        break;
                    case "Red":
                        max = 10000;
                        break;
                    case "Black":
                        max = 1000;
                        break;
                    case "Green":
                    default:
                        max = 100;
                }

                let rand = 1 + Math.random() * max;
                let value = patient.hr;

                console.log(value);

                if ( rand <= (max/3) ) {
                    value++;
                } else if( patient.tt != "Green" && rand == max ){
                    value = 0;
                }else if ( rand >= (2 * (max/3) ) ) {
                    value--;
                }

                this.props.UpdatePatient(patient, value);
            }
        }
    };

    render() {
        return (
            <div className="section">
                <h4>Simulator</h4>
                <div className="divider"/>
                <p className="valign-wrapper">Simulator details coming soon.</p>
                <div className="divider"/>
                <a id="start" href="#" onClick={ this.simButton } className="waves-effect waves-light light-green darken-2 btn">{this.state.text}</a>
            </div>
        )
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        UpdatePatient: (patient, value) => dispatch(UpdatePatient(patient, value))
    }
};

export default connect(null, mapDispatchToProps)(Simulator);