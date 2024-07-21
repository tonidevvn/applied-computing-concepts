'use client'

import React, { useEffect } from 'react'
import { WebCrawlerProps } from '../types/webcrawler'
import axios from 'axios'
import { Input } from 'antd'

function WebCrawler() {
    const [searchValue, setSearchValue] = React.useState('')
    const [result, setResult] = React.useState<WebCrawlerProps>({})

    const fetchData = async () => {
        try {
            const response = await axios.get('/api/web-crawler', {
                params: { url: searchValue },
            })
            setResult(response.data)
        } catch (error) {
            console.error('Error fetching data:', error)
        }
    }

    return (
        <div className='min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4'>
            <div className='bg-white p-6 rounded-lg shadow-md w-3/4'>
                <h1 className='text-2xl font-bold mb-6 text-center'>
                    Web Crawler
                </h1>
                <Input
                    value={searchValue}
                    placeholder='Enter URL'
                    onChange={(e) => setSearchValue(e.target.value)}
                    className='mb-4 px-4 py-2 border rounded-lg w-full'
                />
                <button
                    onClick={fetchData}
                    className='w-full px-4 py-2 bg-blue-500 text-white font-semibold rounded-lg shadow hover:bg-blue-600 transition duration-200'
                >
                    Search
                </button>
                {result.time && (
                    <div className='mt-6'>
                        <p className='font-semibold'>Time:</p>
                        <p className='mb-4'>{result.time}</p>
                        <p className='font-semibold'>Content:</p>
                        <p>{result.htmlContents}</p>
                    </div>
                )}
            </div>
        </div>
    )
}

export default WebCrawler
