'use client'

import { Alert, Button, Card, Form, Input, Skeleton } from 'antd'
import axios from 'axios'
import React, { useState } from 'react'

type FieldType = {
    q?: string
    url?: string
}

type FrequencyCountType = {
    keyword: string
    frequency: number
    count: number
    url: string
    searchTime: string
}

const FrequencyCount: React.FC = () => {
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const [result, setResult] = useState<FrequencyCountType>(
        {} as FrequencyCountType
    )

    const onFinish = async (values: FieldType) => {
        setError('')
        setLoading(true)
        setResult({} as FrequencyCountType)
        await axios
            .get('/api/frequency-count', {
                params: values,
            })
            .then((response) => {
                setResult(response.data)
            })
            .catch((error) => {
                setError('Invalid URL. Please try again')
                setResult({} as FrequencyCountType)
            })
            .finally(() => {
                setLoading(false)
            })
    }

    const onFinishFailed = (errorInfo: any) => {
        console.error('Failed:', errorInfo)
    }
    return (
        <div className='min-h-screen flex flex-col items-center justify-center p-4'>
            <Card
                title='Search Frequency & Count'
                className='bg-white p-4 rounded-lg shadow-md w-1/2 grid grid-rows-1 gap-4'
            >
                <Form
                    name='basic'
                    labelCol={{ span: 4 }}
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete='off'
                >
                    <Form.Item<FieldType>
                        label='Keyword'
                        name='q'
                        rules={[
                            {
                                required: true,
                                message: 'Please input your keyword!',
                            },
                        ]}
                        className='mb-4'
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item<FieldType>
                        label='Url'
                        name='url'
                        rules={[
                            {
                                required: true,
                                message: 'Please input your URL!',
                            },
                        ]}
                        className='mb-4'
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item className='text-center'>
                        <Button type='primary' htmlType='submit'>
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
                {loading && <Skeleton loading />}
                {error && <Alert message={error} type='error' />}
                {result.keyword && (
                    <div>
                        <p>
                            There are a total of {result.frequency} occurrences
                            of the keyword on the page.
                        </p>
                        <p>This word has been searched {result.count} times.</p>
                    </div>
                )}
            </Card>
        </div>
    )
}

export default FrequencyCount
