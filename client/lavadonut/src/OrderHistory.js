import React, { use } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  scales,
} from "chart.js";
import { Container, Paper, Box, Typography, Alert } from "@mui/material";
import { Bar } from "react-chartjs-2";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

// USE EFFECT TO FETCH PORTFOLIO FROM URL
//REFERENCE CONTEXT TO GET USER OBJ OR ID

function OrderHistory() {
  const [portfolio, setPortfolio] = useState(); // one portfolio
  const [userOrders, setOrders] = useState([]);
  const [userStocks, setStocks] = useState([]);
  const [displayInfo, setDisplayInfo] = useState([]);
  const [error, setError] = useState("");

  const { id } = useParams();

  const token =
    localStorage.getItem("token") || sessionStorage.getItem("token");
  const url = `http://localhost:8080/api/portfolio/port`;

  useEffect(() => {
    fetch(`${url}/${id}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => {
        setPortfolio(data);
        sortStocksOrders(data);
        processDisplayInfo(data);
      }).catch((error) => {
        setError(error.message || "Portfolio failed to load!");
      });
  }, [token, url, id]);

  /*
  const userOrders = portfolio.orders;
  userOrders.sort();
  const userStocks = portfolio.stocks;
  userStocks.sort();
  */

  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: `Your Order History`,
      },
    },
  };

  const orderHistory = {
    labels: userStocks.map((stock) => stock.name), // check if order and stock are same before
    datasets: [
      {
        label: "Cost",
        data: userOrders.map((order) => order.price), //need to dynamically introduce data
        backgroundColor: ["rgba(255, 99, 132, 0.2)"],
        borderColor: ["rgba(255, 99, 132, 0.2)"],
        borderWidth: 1,
      },
    ],
  };

  function sortStocksOrders(data) {
    setOrders(data.orders.sort((a, b) => a.stockId - b.stockId));
    setStocks(data.stocks.sort((a, b) => a.id - b.id));
    /*
    let totalInfo = []
    for(let i = 0; i < data.orders.length; i++){
      let currentOrder = data.orders.at(i);
      let currentStock = data.stocks.at(i);
      totalInfo.push(
        {
          orderTransactionType : currentOrder.transactionType,
          orderSharesAmount : currentOrder.numberOfShares,
          orderPrice : currentOrder.price,
          stockName : currentStock.name,
          stock : currentStock.currentPrice
        }
      )
    }
    console.log("HERE")
    console.log(totalInfo);
    */
    return;
  }

  function processDisplayInfo(data) {
    let orderedOrders = data.orders.sort((a, b) => a.stockId - b.stockId);
    let orderedStocks = data.stocks.sort((a, b) => a.id - b.id);

    let totalInfo = []
    for(let i = 0; i < orderedOrders.length; i++){
      let currentOrder = orderedOrders.at(i);
      let currentStock = orderedStocks.at(i);
      totalInfo.push(
        {
          orderTransactionType : currentOrder.transactionType,
          orderSharesAmount : currentOrder.numberOfShares,
          orderPrice : currentOrder.price,
          stockName : currentStock.name,
          stock : currentStock.currentPrice
        }
      )
    }

    setDisplayInfo(totalInfo);

    return;
  }

  function getStockName(orderStockId) {
    console.log("I am being called");
    userStocks.forEach((stock) => {
      console.log("Iterating through for each")
      if (stock.id === orderStockId) {
        console.log(stock)
        return stock.id;
      }
    });
  }

  function getStockPrice(orderStockId) {
    userStocks.forEach((stock) => {
      if (stock.id === orderStockId) {
        return stock.currentPrice;
      }
    });
  }

  // format page in to display stock and order info
  return (
    <>
      {portfolio ? (
        <>
        {console.log(portfolio)}
        {console.log(userStocks)}
        {console.log(Array.isArray(portfolio))}
          <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
              {error && (
                <Alert severity="error" sx={{ mb: 4 }}>
                  {error}
                </Alert>
              )}
              {userOrders.map((order) => (
                <Box sx={{ display: "flex", gap: 3, overflowX: "auto", pb: 2, mt: 3 }}>
                  <Typography variant="h2" align="center">
                    {getStockName(order.stockId)}
                  </Typography>
                  <Typography variant="body1">Date: ${order.date}</Typography>
                  <Typography variant="body1">
                    Transaction Type: {order.transactionType}
                  </Typography>
                  <Typography variant="body1">
                    Shares: {order.numberOfShares}
                  </Typography>
                  <Typography variant="body1">
                    Total Price: ${order.price}
                  </Typography>
                  <Typography variant="body1">
                    Stock Price: ${(getStockPrice(order.stockId))}
                  </Typography>
                </Box>
              ))}
              <Bar options={options} data={orderHistory} />
            </Paper>
          </Container>
        </>
      ) : (
        <div> Loading... </div>
      )}
    </>
  );
}

export default OrderHistory;
