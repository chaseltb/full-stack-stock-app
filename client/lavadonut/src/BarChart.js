import { plugins } from "chart.js";
import React from "react";
import { Bar } from "react-chartjs-2"

export const BarChart = ( { chartData }) => {
    return (
        <>
            <div className = "chart-container">
                <h2 style = {{ textAlign: "center" }}> Bar Chart </h2>
                <Bar
                    data = {chartData}
                    otions={{
                        plugins: {
                            title : {
                                display: true,
                                text: "PLACEHOLDER TITLE"
                            },
                            legend: {
                                display: false
                            }
                        }
                    }}
                />
            </div>
        </>
    );
};