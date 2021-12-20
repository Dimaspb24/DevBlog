import React from "react"
import IconButton from '@mui/material/IconButton'
import InputLabel from '@mui/material/InputLabel'
import FormControl from '@mui/material/FormControl'
import OutlinedInput from '@mui/material/OutlinedInput'
import InputAdornment from '@mui/material/InputAdornment'
import Visibility from '@mui/icons-material/Visibility'
import VisibilityOff from '@mui/icons-material/VisibilityOff'
import TextField from '@mui/material/TextField'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import GoogleIcon from '@mui/icons-material/Google'
import SendIcon from '@mui/icons-material/Send'
import Typography from '@mui/material/Typography'
import Card from '@mui/material/Card'

const LoginPage: React.FC = (props) => {
    const [values, setValues] = React.useState({
        username: '',
        password: '',
        showPassword: false,
    });

    const handleChange = (prop: any) => (event: any) => {
        setValues({...values, [prop]: event.target.value});
    }

    const handleClickShowPassword = () => {
        setValues({
            ...values,
            showPassword: !values.showPassword,
        })
    }

    const handleMouseDownPassword = (event: any) => {
        event.preventDefault();
    }

    return (
        <Box sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            backgroundColor: '#e7f7fe'
        }}
        >
            <Card
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    m: '10ch',
                    maxWidth: '65ch',
                    borderRadius: 5,
                    border: '3px solid #ab47bc'
                }}
            >

                <Typography gutterBottom variant="h4"
                            component="div"
                            sx={{p: '3ch', fontWeight: '600', color: '#9c27b0'}}
                >
                    Welcome to IDK Community
                </Typography>

                <Button
                    sx={{
                        mb: '3ch',
                        width: '35ch',
                        backgroundColor: '#9c27b0'
                    }}
                    size="large"
                    variant="contained"
                    startIcon={<GoogleIcon/>}
                >
                    Sign in with Google
                </Button>

                <TextField
                    id="outlined-name"
                    sx={{m: 1, width: '35ch'}}
                    variant="outlined"
                    label="Email"
                    value={values.username}
                    onChange={handleChange('username')}
                />

                <FormControl sx={{m: 1, width: '35ch'}} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password"
                        type={values.showPassword ? 'text' : 'password'}
                        value={values.password}
                        onChange={handleChange('password')}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {values.showPassword ? <VisibilityOff/> : <Visibility/>}
                                </IconButton>
                            </InputAdornment>
                        }
                        label="Password"
                    />
                </FormControl>

                <Button
                    sx={{
                        mt: '1ch',
                        mb: '10ch',
                        width: '18ch',
                        backgroundColor: '#9c27b0'
                    }}
                    size="large"
                    variant="contained"
                    endIcon={<SendIcon/>}
                >
                    Login
                </Button>
            </Card>
        </Box>
    )
}

export default LoginPage
